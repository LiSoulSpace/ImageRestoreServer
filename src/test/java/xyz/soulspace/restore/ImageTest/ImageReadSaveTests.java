package xyz.soulspace.restore.ImageTest;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.mapper.ImageInfoMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest
@Slf4j
public class ImageReadSaveTests {
    @Autowired
    ImageInfoMapper imageInfoMapper;

    @Test
    void getImageInfoPage() {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllPage(1, 10, null);
        log.info("{}", imageInfos.stream().map(ImageInfo::getImagePath).toList());
    }

    @Test
    void readImagePathsIPage() {
        IPage<ImageInfo> iPage1 = imageInfoMapper.selectPage(
                new Page<>(1, 10, false),
                new QueryWrapper<>()
        );
        long pages = iPage1.getPages();
        long total = iPage1.getTotal();
        List<ImageInfo> records = iPage1.getRecords();
        long current = iPage1.getCurrent();
        long size = iPage1.getSize();

        log.info("records:{},\n records_size:{} " +
                        "\nsize:{},\ntotal:{},\ncurrent:{},\n" +
                        "pages:{}\n", records, records.size(),
                size, total, current, pages);
    }

    @Test
    void getJiaRanInfo() throws IOException, ImageProcessingException {
        Path path = Path.of("/home/soulspace/Documents/GitHub/ImageRestoreServer/img/avatar/jiaran.webp");
        InputStream imageInputStream = Files.newInputStream(path);
        Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                log.info("{}---{}", tagName, desc);
            }
        }
    }

    @Test
    void getPngInfo() throws IOException, ImageProcessingException {
        String fileName = "2022_07_13_Uplnek_Lubovna_1000mm_1500px.png";
        String dirPath = "/home/soulspace/Documents/GitHub/ImageRestoreServer/img/wallpaper/nasa_apod/";
        Path path = Path.of(dirPath + fileName);
        InputStream imageInputStream = Files.newInputStream(path);
        Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                log.info("{}---{}", tagName, desc);
            }
        }
    }

    @Test
    void getJpgInfo() throws IOException, ImageProcessingException {
        Path path = Path.of("/home/soulspace/Documents/GitHub/ImageRestoreServer/img/wallpaper/nasa_apod/2021_03_02_Mars_Taurus_1800px.jpg");
        InputStream imageInputStream = Files.newInputStream(path);
        Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                log.info("{}---{}", tagName, desc);
            }
        }
    }

    @Test
    void addImageInfoToDB() throws IOException {
        Path imageWallpaper = Path.of("/home/soulspace/Documents/GitHub/ImageRestoreServer/img/wallpaper");
        AtomicBoolean test = new AtomicBoolean(true);
        Files.list(imageWallpaper).forEach(file -> {
            if (Files.isDirectory(file)) {
                try {
                    Files.list(file).forEach(image -> {
                        try {
                            if (true) {
                                InputStream imageInputStream = Files.newInputStream(image);
                                Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
                                HashMap<String, String> map = new HashMap<>();
                                Set<String> set = new HashSet<>();
                                set.add("Detected File Type Name");
                                set.add("Image Height");
                                set.add("Image Width");
                                set.add("Number of Components");
                                map.put("imageName", String.valueOf(image.getFileName()));
                                map.put("imagePath", image.toString().substring(51));
                                String fileMD5 = MD5.create().digestHex(imageInputStream);
                                map.put("imageMd5", fileMD5);
                                for (Directory directory : metadata.getDirectories()) {
                                    for (Tag tag : directory.getTags()) {
                                        if (test.get()) log.info("{}", tag);
                                        if (set.contains(tag.getTagName())) {
                                            if (tag.getTagName().equals("Image Height") ||
                                                    tag.getTagName().equals("Image Width")) {
                                                map.put(tag.getTagName(), tag.getDescription().split(" ")[0]);
                                            } else {
                                                String tagName = tag.getTagName();  //标签名
                                                String desc = tag.getDescription(); //标签信息
                                                map.put(tagName, desc);
                                            }
                                        }
                                    }
                                }
                                test.set(false);
                                log.info("{}", map);
//                                ImageInfo imageInfo = new ImageInfo();
//                                imageInfo.setImageType(map.get("Detected File Type Name"));
//                                imageInfo.setImageMd5(map.get("imageMd5"));
//                                imageInfo.setImageHeight(Integer.valueOf(map.get("Image Height")));
//                                imageInfo.setImageWidth(Integer.valueOf(map.get("Image Width")));
//                                imageInfo.setImagePath(map.get("imagePath"));
//                                imageInfo.setImageName(map.get("imageName"));
//                                imageInfoMapper.insert(imageInfo);
                                imageInputStream.close();
                            }
                        } catch (ImageProcessingException | IOException e) {
                            log.error("{}", e);
                        }


                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
