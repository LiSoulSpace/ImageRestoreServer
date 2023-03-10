package xyz.soulspace.restore;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import xyz.soulspace.restore.entity.ImageInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FreeTest {
    @Test
    public void testForFastJson() {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setId(1L);
        imageInfo.setImageName("test");
        imageInfo.setImagePath("test1");
        imageInfo.setImageMd5("test3");
        imageInfo.setImageWidth(3);
        imageInfo.setImageHeight(3);
        imageInfo.setCreateTime(LocalDateTime.now());
        imageInfo.setUpdateTime(LocalDateTime.now());
        imageInfo.setImageType("est");
        String s = JSON.toJSONString(imageInfo);
        List<ImageInfo> list = new ArrayList<>();
        list.add(imageInfo);
        list.add(imageInfo);
        log.info("{}", s);
        log.info("{}", JSON.toJSONString(list));
    }
}
