package xyz.soulspace.restore.ImageTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.mapper.ImageInfoMapper;

import java.util.List;

@SpringBootTest
@Slf4j
public class ImageInfoTest {

    @Autowired
    ImageInfoMapper imageInfoMapper;

    @Test
    void getInfoByMd5Test() {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllByImageMd5("");
        log.info("{}", imageInfos);
    }
}
