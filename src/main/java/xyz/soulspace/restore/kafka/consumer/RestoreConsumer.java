package xyz.soulspace.restore.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import xyz.soulspace.restore.redis.RedisService;

@Component
@Slf4j
public class RestoreConsumer {
    @Autowired
    RedisService redisService;

    @KafkaListener(topics = {"topic-restore-answer"},
            topicPartitions = {@TopicPartition(topic = "topic-restore-answer", partitions = {"0"})},
            groupId = "topic-restore-answer-group")
    public void onRestoreAnswer(ConsumerRecord<?, ?> record) {
        String key = (String) record.key();
        log.info("key:[{}], value:[{}], partition:[{}], topic:[{}]",
                record.key(), record.value(), record.partition(), record.topic());
        if (key.equals("image_restore_answer")) {
            log.info("{}", "接收到信息");
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
