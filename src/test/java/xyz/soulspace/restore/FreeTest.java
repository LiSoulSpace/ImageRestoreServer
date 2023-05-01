package xyz.soulspace.restore;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import xyz.soulspace.restore.entity.ImageInfo;

import java.time.LocalDateTime;

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
        JSONObject o = (JSONObject) JSON.toJSON(imageInfo);
        o.put("userId", 1);
        log.info("{}", o);
//        List<ImageInfo> list = new ArrayList<>();
//        list.add(imageInfo);
//        list.add(imageInfo);
//        log.info("{}", s);
//        log.info("{}", JSON.toJSONString(list));
    }

    @Test
    void testFastJson() {
        Object parse = JSON.parse("{\"hello\":\"test\"}");
        log.info("{}", parse);
        Object parse1 = JSON.parse("""
                [{"hello":"test"}, {"test":"hello"}]
                """);
        log.info("{}", parse1);
    }

    @Test
    void testParseObject() {
        String s = "{\"id\":2018, \"result\":\"/home/soulspace/Documents/GitHub/ImageRestoreServer/img/img_small/pexels/cat/cat_33_small.jpeg\"} ";
        JSONObject jsonObject = JSON.parseObject(s);
        log.info(jsonObject.toString());
    }

    @Test
    void testListInJSON(){
        String s = "hello,test,test2";
        String[] split = s.split(",");
        Object json = JSON.toJSON(split);
        System.out.println(json);
    }
}
