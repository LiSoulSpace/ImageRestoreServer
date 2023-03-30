package xyz.soulspace.restore.ImageTest;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.entity.Tag;
import xyz.soulspace.restore.mapper.TagMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class TagTest {

    @Autowired
    TagMapper tagMapper;

    @Test
    void tagsAllFind() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("风景");
        tagNames.add("壁纸");
        List<Tag> allByTagNames = tagMapper.findAllByTagNames(tagNames);
        log.info("{}", JSON.toJSONString(allByTagNames));
    }

    @Test
    void test() {
        List<String> tagNames = new ArrayList<>();
        tagNames.add("风景");
        tagNames.add("壁纸");
        List<Tag> allByTagNames = tagMapper.findAllByTagNames(tagNames);
        List<Long> longs = allByTagNames.stream().map(Tag::getId).toList();
        List<ImageInfo> imageByTag = tagMapper.findImageByTag(longs);
        log.info("{}", JSON.toJSONString(imageByTag));
    }
}
