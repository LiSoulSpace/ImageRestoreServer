package xyz.soulspace.restore.ImageTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.mapper.ImageInfoMapper;
import xyz.soulspace.restore.service.ImageInfoService;

import java.nio.file.Path;
import java.util.List;

@Slf4j
@SpringBootTest
public class ImageFixTest {
    @Autowired
    ImageInfoMapper imageInfoMapper;
    @Autowired
    ImageInfoService imageInfoService;
    @Value("${image.dataPath}")
    String dataPath;

    @Test
    void testAllFix() {
        for (int i = 1; i < 360; i++) {
            List<ImageInfo> infos = imageInfoMapper.selectById((long) i);
            if (infos.size() > 0) {
                CommonResult<?> commonResult = imageInfoService.imageFixSmallById((long) i);
                if (!commonResult.isSuccess()) {
                    log.error("{}", commonResult);
                }
            }
        }
    }

    @Test
    void testForReturnNull(){
        Long aLong = imageInfoMapper.selectSmallByOrigin(888L);
        log.info("{}", aLong);
    }


}
