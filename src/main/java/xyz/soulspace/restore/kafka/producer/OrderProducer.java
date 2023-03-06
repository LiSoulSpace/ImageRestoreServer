package xyz.soulspace.restore.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import xyz.soulspace.cinemaline.entity.Order;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OrderProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public boolean sendOrder(Order order) {
        try {
            String orderJson = JSON.toJSONString(order);
            log.warn("准备发送订单[{}]", orderJson);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(
                    "topic-test", 2,
                    "order", orderJson);
            send.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.warn(ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.warn("订单发送完成[{}]", result.toString());
                }
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean sendNewSeatInfo(String orderSeats, Long processId) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("name", "seatsInfo");
            map.put("orderSeats", orderSeats);
            map.put("processId", String.valueOf(processId));
            String mapJson = JSON.toJSONString(map);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(
                    "topic-test", 1, "seatsInfo", mapJson);
            send.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error(ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.debug("successfully send seatInfo{}", result.toString());
                }
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public boolean sendOrderRefund(Long orderId) {
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(
                "topic-test", 2, "orderRefund", String.valueOf(orderId));
        send.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.warn(result.toString());
            }
        });
        return true;
    }

    public String sendString(String order) {
        ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send("topic-test", 0, "string", order);
        send.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error(ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.warn(result.toString());
            }
        });
        return "Send OK";
    }
}
