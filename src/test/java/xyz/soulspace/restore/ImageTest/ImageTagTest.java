package xyz.soulspace.restore.ImageTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.soulspace.restore.entity.ImageTagRelation;
import xyz.soulspace.restore.mapper.ImageTagRelationMapper;

@SpringBootTest
@Slf4j
public class ImageTagTest {
    @Autowired
    ImageTagRelationMapper imageTagRelationMapper;

    @Test
    void insertNewRelation() {
        for (int i = 1539; i <= 2183; i++) {
            ImageTagRelation imageTagRelation = new ImageTagRelation();
            imageTagRelation.setTagId(13L);
            imageTagRelation.setImageId((long) i);
            imageTagRelationMapper.insert(imageTagRelation);
        }
    }

    @Test
    void insertMountainRelation() {
        for (int i = 718; i <= 1538; i++) {
            ImageTagRelation imageTagRelation = new ImageTagRelation();
            imageTagRelation.setTagId(13L);
            imageTagRelation.setImageId((long) i);
            imageTagRelationMapper.insert(imageTagRelation);
        }
    }
}
