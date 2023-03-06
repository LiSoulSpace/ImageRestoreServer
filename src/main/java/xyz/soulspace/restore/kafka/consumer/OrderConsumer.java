package xyz.soulspace.restore.kafka.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import xyz.soulspace.cinemaline.dto.SeatDTO;
import xyz.soulspace.cinemaline.entity.Order;
import xyz.soulspace.cinemaline.entity.OrderExtraInfo;
import xyz.soulspace.cinemaline.entity.SeatInfo;
import xyz.soulspace.cinemaline.mapper.OrderExtraInfoMapper;
import xyz.soulspace.cinemaline.mapper.OrderMapper;
import xyz.soulspace.cinemaline.mapper.SeatInfoMapper;
import xyz.soulspace.cinemaline.redis.RedisService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class OrderConsumer {
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    SeatInfoMapper seatInfoMapper;
    @Autowired
    OrderExtraInfoMapper orderExtraInfoMapper;
    @Autowired
    RedisService redisService;

    @KafkaListener(topics = {"topic-test"}, topicPartitions = {@TopicPartition(topic = "topic-test", partitions = {"2"})}, groupId = "test-consumer-group")
    public void onOrder(ConsumerRecord<?, ?> record) {
        String key = (String) record.key();
         if (key.equals("order")) {
            log.info("Topic:[{}], 分区:[{}], key:[{}], value:[{}]",
                    record.topic(), record.partition(), record.key(), record.value());
            try {
                Order order = JSON.parseObject(record.value().toString(), Order.class);
                int insert = orderMapper.insert(order);
                if (insert > 0) log.info("Order[{}] insert finished!", order.getId().toString());
                else log.error("订单入库失败");
                OrderExtraInfo orderExtraInfo = new OrderExtraInfo();
                orderExtraInfo.setOrderId(order.getId());
                insert = orderExtraInfoMapper.insert(orderExtraInfo);
                if (insert > 0) log.info("Order Extra Info [{}] insert finished!", order.getId().toString());
                else log.error("订单入库失败");
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        } else if (key.equals("orderRefund")) {
            log.warn("Topic:[{}], 分区:[{}], key:[{}], value:[{}]",
                    record.topic(), record.partition(), record.key(), record.value());
            String value = (String) record.value();
            Long orderId = Long.valueOf(value);
            List<Order> orders = orderMapper.selectAllById(orderId);
            String seatInfo = orders.get(0).getSeatInfo();
            List<Order> selectProcessIdById = orderMapper.selectProcessIdById(orderId);
            Long processId = selectProcessIdById.get(0).getProcessId();
            List<SeatInfo> seatInfos = seatInfoMapper.selectSeatArrangementByProcessId(processId);
            String[] seatsStrings = seatInfo.split(" ");
            List<List<Integer>> seats = new ArrayList<>();
            seats.add(new ArrayList<>());
            seats.add(new ArrayList<>());
            for (String s : seatsStrings) {
                redisService.sAdd("set:seatArrangement:" + processId, s);
                if (s.length() > 3) {
                    String[] split = s.split(",");
                    String rowT = split[0].trim();
                    String colT = split[1].trim();
                    seats.get(0).add(Integer.valueOf(rowT.substring(1)));
                    seats.get(1).add(Integer.valueOf(colT.substring(0, colT.length() - 1)));
                }
            }
            SeatDTO seatDTO = JSON.parseObject(seatInfos.get(0).getSeatArrangement(), SeatDTO.class, Feature.DisableCircularReferenceDetect);
            for (int i = 0; i < seats.get(0).size(); i++) {
                seatDTO.setLocation(seats.get(0).get(i), seats.get(1).get(i), 0);
            }
            String s = seatDTO.seatsToString();
            log.debug("写回的座位信息：{}", s);
            int i = seatInfoMapper.setSeatArrangementByProcessId(s, processId);
            if (i > 0) {
                log.debug("写回成功");
            }
        }
    }

    @KafkaListener(topics = {"topic-test"}, topicPartitions = {@TopicPartition(topic = "topic-test", partitions = {"1"})}, groupId = "")
    public void onSeatsInfo(ConsumerRecord<?, ?> record) {
        String key = (String) record.key();
        if (key.equals("seatsInfo")) {
            log.info("Topic:[{}], 分区:[{}], key:[{}], value:[{}]",
                    record.topic(), record.partition(), record.key(), record.value());
            try {
                HashMap<String, String> test = new HashMap<>();
                HashMap<String, String> value = JSON.parseObject(record.value().toString(), test.getClass());
                String processString = value.get("processId");
                Long processId = Long.valueOf(processString);
                String orderSeats = value.get("orderSeats");
                List<SeatInfo> seatInfos = seatInfoMapper.selectSeatArrangementByProcessId(processId);
                if (seatInfos.size() <= 0) {
                    log.error("无{}对应的座位信息", processId);
                } else {
                    log.info("orderedSeats:{}", orderSeats);
                    String[] seatsStrings = orderSeats.split(" ");
                    List<List<Integer>> seats = new ArrayList<>();
                    seats.add(new ArrayList<>());
                    seats.add(new ArrayList<>());
                    for (String s : seatsStrings) {
                        if (s.length() > 3) {
                            String[] split = s.split(",");
                            String rowT = split[0].trim();
                            String colT = split[1].trim();
                            seats.get(0).add(Integer.valueOf(rowT.substring(1)));
                            seats.get(1).add(Integer.valueOf(colT.substring(0, colT.length() - 1)));
                        }
                    }
                    log.info("预定座位信息解析:{}", seats);
                    SeatDTO seatDTO = JSON.parseObject(seatInfos.get(0).getSeatArrangement(), SeatDTO.class, Feature.DisableCircularReferenceDetect);
                    for (int i = 0; i < seats.get(0).size(); i++) {
                        seatDTO.setLocation(seats.get(0).get(i), seats.get(1).get(i), 2);
                    }
                    String s = seatDTO.seatsToString();
                    log.info("写回的座位信息：{}", s);
                    int i = seatInfoMapper.setSeatArrangementByProcessId(s, processId);
                    if (i > 0) {
                        log.info("写回成功");
                    }
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    @KafkaListener(topics = {"topic-test"}, topicPartitions = {@TopicPartition(topic = "topic-test", partitions = {"0"})}, groupId = "")
    public void onString(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费Topic：" + record.topic() + "**分区" + record.partition() + "**值内容" + record.value());
    }
}
