package xyz.soulspace.restore.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import xyz.soulspace.restore.entity.ImageInfo;

@Slf4j
@Component
public class RestoreProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private String imagePath;

    public boolean sendImageInfo(ImageInfo imageInfo) {
        try {
            String imageInfoJson = JSON.toJSONString(imageInfo);
            log.info("需要转换的图像信息 : [{}]", imageInfoJson);
            ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(
                    "topic-image-restore", 0,
                    "imageInfo", imageInfoJson);
            send.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    log.error(ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    log.info("图像信息发送完成 : [{}]", result.toString());
                }
            });
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
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
