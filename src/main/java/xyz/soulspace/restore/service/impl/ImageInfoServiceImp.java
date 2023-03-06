package xyz.soulspace.restore.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.MD5;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.mapper.ImageInfoMapper;
import xyz.soulspace.restore.service.ImageInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 07:04:23
 */
@Service
@Slf4j
public class ImageInfoServiceImp extends ServiceImpl<ImageInfoMapper, ImageInfo> implements ImageInfoService {

    @Autowired
    ImageInfoMapper imageInfoMapper;
    @Value("${strings.address}")
    String address;
    @Value("${image.dataPath}")
    String dataPath;
    @Value("${image.userImagePath}")
    String userImageDir;

    @Override
    public List<String> getImagePathPage(Integer currentPage, Integer pageSize) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllPage(currentPage, pageSize, null);
        return imageInfos.stream().map(e -> address + e.getImagePath()).toList();
    }

    @Override
    public List<ImageInfo> getImageInfoPage(Integer currentPage, Integer pageSize) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllPage(currentPage, pageSize, null);
        return imageInfos;
    }

    @Override
    public List<ImageInfo> getImageInfoPageByUserId(Integer currentPage, Integer pageSize, Long userId) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllPage(currentPage, pageSize, userId);
        return imageInfos;
    }

    @Override
    public ResponseEntity<?> uploadImageByUserId(Long userId, MultipartFile imageUpload) {
        String contentType = imageUpload.getContentType();
        String name = imageUpload.getName();
        String originalFilename = imageUpload.getOriginalFilename();
        assert contentType != null;
        String[] split = contentType.split("/");
        if (!split[0].equals("image")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(CommonResult.failed(1, "请上传图像文件", null));
        } else {
            log.info("ContentType: {}\nname: {}\noriginalFilename: {}",
                    contentType, name, originalFilename);
            String format = String.format("[ContentType: %s] [name: %s] [originalFilename: %s]",
                    contentType, name, originalFilename);
            String encode = Base64.encode(String.valueOf(userId));
            Path thisUserImagePath = Path.of(String.format("%s/%s", userImageDir, encode));
            if (!Files.exists(thisUserImagePath)) {
                try {
                    Files.createDirectory(thisUserImagePath);
                } catch (IOException e) {
                    log.warn(e.getMessage());
                    return ResponseEntity.internalServerError()
                            .body(CommonResult.failed(4, "用户图像文件夹创建失败", e.getMessage()));
                }
            }
            Path fileSaveAbsolutePath = Paths.get(String.valueOf(thisUserImagePath), originalFilename);
            try {
                imageUpload.transferTo(fileSaveAbsolutePath);
                CommonResult<?> saveImageInfoResult = saveImageInfo(fileSaveAbsolutePath);
                if (saveImageInfoResult.isSuccess())
                    return ResponseEntity.ok(CommonResult.success("文件上传完成", format));
                else return ResponseEntity.internalServerError().body(saveImageInfoResult);
            } catch (IOException e) {
                log.error(e.getMessage());
                return ResponseEntity.internalServerError()
                        .body(CommonResult.failed(3, "图像保存失败", e.getMessage()));
            }
        }
    }

    @Override
    public CommonResult<?> saveImageInfo(Path image) {
        try (InputStream imageInputStream = Files.newInputStream(image)) {
            Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
            HashMap<String, String> map = new HashMap<>();
            Set<String> set = new HashSet<>();
            set.add("Detected File Type Name");
            set.add("Image Height");
            set.add("Image Width");
            map.put("imageName", String.valueOf(image.getFileName()));
            map.put("imagePath", image.toString().substring(dataPath.length()));
            String fileMD5 = MD5.create().digestHex(imageInputStream);
            map.put("imageMd5", fileMD5);

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
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
            log.info("Map of savedImageInfo_{} : {}", image, map);
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setImageType(map.get("Detected File Type Name"));
            imageInfo.setImageMd5(map.get("imageMd5"));
            imageInfo.setImageHeight(Integer.valueOf(map.get("Image Height")));
            imageInfo.setImageWidth(Integer.valueOf(map.get("Image Width")));
            imageInfo.setImagePath(map.get("imagePath"));
            imageInfo.setImageName(map.get("imageName"));
            imageInfoMapper.insert(imageInfo);
            return CommonResult.success("图像保存成功", imageInfo);
        } catch (ImageProcessingException | IOException e) {
            log.error(e.getMessage());
            return CommonResult.failed(1, "图像保存失败", e.getMessage());
        }
    }

    @Override
    public CommonResult<?> saveUserImageRelation(ImageInfo savedImageInfo, Long userId) {
        int i = imageInfoMapper.insertUserImageInfo(userId, savedImageInfo.getId());
        if (i > 0) return CommonResult.success("用户图像关系保存成功");
        return CommonResult.failed(1, "用户图像关系保存失败", null);
    }

    @Override
    public CommonResult<?> getImagePathById(Long id) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectImagePathById(id);
        if (imageInfos.size() == 0) return CommonResult.failed(1, "没有查找到图片", null);
        return CommonResult.success(imageInfos.get(0));
    }
}
