package xyz.soulspace.restore.kafkaTest;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.service.ImageInfoService;

import java.util.Scanner;

@Slf4j
@SpringBootTest
public class kafkaTest {
    @Autowired
    ImageInfoService imageInfoService;
    @Test
    void sendImageInfo(){
        ImageInfo imageInfoById = imageInfoService.getById(1);
        String s = JSON.toJSONString(imageInfoById);
        log.info(s);
        CommonResult<?> commonResult = imageInfoService.imageRestoreById(1L);
        log.info("{}", commonResult);
    }
}
