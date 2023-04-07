package xyz.soulspace.restore.kafka.consumer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.redis.RedisService;
import xyz.soulspace.restore.service.ImageInfoService;

import java.nio.file.Path;
import java.util.Map;

@Component
@Slf4j
public class RestoreConsumer {
    @Autowired
    RedisService redisService;
    @Autowired
    ImageInfoService imageInfoService;

    @KafkaListener(topics = {"topic-restore-answer"},
            topicPartitions = {@TopicPartition(topic = "topic-restore-answer", partitions = {"0"})},
            groupId = "topic-restore-answer-group")
    public void onRestoreAnswer(ConsumerRecord<?, ?> record) {
        String key = (String) record.key();
        log.info("key:[{}], value:[{}], partition:[{}], topic:[{}]",
                record.key(), record.value(), record.partition(), record.topic());
        if (key.equals("image_restore_answer")) {
            log.info("Answer from 图像修复:[{}]", record.value());
        }else if(key.equals("image_resize_answer")){
            log.info("Answer from 图像缩略:[{}]", record.value());
            log.info("开始作后续处理");
            CommonResult<?> result = JSON.parseObject((String) record.value(), CommonResult.class);
            log.info("{}", result.getData());
            JSONObject jsonObject = JSONObject.parseObject((String) result.getData());
            //json对象转Map
            Map<String, Object> map = (Map<String, Object>) jsonObject;
            int id = (Integer) map.get("id");
            String smallImagePath = (String) map.get("result");
            CommonResult<?> commonResult = imageInfoService.saveImageInfo(Path.of(smallImagePath));
            if (commonResult.isSuccess()) {
                ImageInfo data = (ImageInfo) commonResult.getData();
                CommonResult<?> commonResult1 = imageInfoService.insertOriginSmallRelation((long) id, data.getId());
                if (commonResult1.isSuccess())
                    log.info("图像信息保存成功:[{}]-[{}]", id, data.getId());
            } else {
                log.info("{}", commonResult);
            }
            log.info("{}:[{}][{}]", "接收到信息", id, smallImagePath);
        }
    }

    @KafkaListener(topics = {"topic-test"},
            topicPartitions = {@TopicPartition(topic = "topic-test", partitions = {"0"})},
            groupId = "")
    public void onString(ConsumerRecord<?, ?> record) {
        // 消费的哪个topic、partition的消息,打印出消息内容
        log.info("简单消费Topic：" + record.topic() + "**分区" + record.partition() + "**值内容" + record.value());
    }
}
