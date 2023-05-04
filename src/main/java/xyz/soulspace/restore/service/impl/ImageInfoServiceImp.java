package xyz.soulspace.restore.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.dto.ImageBaseInfoDTO;
import xyz.soulspace.restore.dto.ImgMaxWidHei;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.kafka.producer.RestoreProducer;
import xyz.soulspace.restore.mapper.ImageInfoMapper;
import xyz.soulspace.restore.service.ImageInfoService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
@Transactional
public class ImageInfoServiceImp extends ServiceImpl<ImageInfoMapper, ImageInfo> implements ImageInfoService {

    @Autowired
    ImageInfoMapper imageInfoMapper;
    @Value("${strings.address}")
    String address;
    @Value("${image.dataPath}")
    String dataPath;
    @Value("${image.userImagePath}")
    String userImageDir;
    @Autowired
    RestoreProducer restoreProducer;

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

    /**
     * @return
     */
    @Override
    public CommonResult<?> getImageInfoCount() {
        try {
            Integer integer = imageInfoMapper.countAll();
            return CommonResult.success(integer);
        } catch (Exception e) {
            return CommonResult.failed(1, "获取图像信息表数据数量出错", "");
        }
    }

    /**
     * @return 
     */
    @Override
    public CommonResult<?> getImageMaxWidHei() {
        try {
            ImgMaxWidHei imgMaxWidHei = imageInfoMapper.selectImageMaxWidHei();
            return CommonResult.success(JSON.toJSONString(imgMaxWidHei));
        }catch (Exception e){
            return CommonResult.failed(1, "获取图像最大宽度高度失败", "");
        }
    }

    /**
     * @param currentPage 当前页码
     * @param pageSize    每页的数量
     * @return
     */
    @Override
    public CommonResult<?> getImageBaseInfoPage(Integer currentPage, Integer pageSize) {
        try {
            List<ImageBaseInfoDTO> baseInfoDTOS = imageInfoMapper.selectImageBaseInfoPage(currentPage, pageSize, null);
            return CommonResult.success("success", JSON.toJSONString(baseInfoDTOS));
        } catch (Exception e) {
            log.error(e.toString());
            return CommonResult.failed(1, "获取图像基本信息失败", "[]");
        }
    }

    /**
     * @param currentPage 当前页码
     * @param pageSize    每页的数量
     * @param userId      用户id
     * @return
     */
    @Override
    public CommonResult<?> getImageBaseInfoPage(Integer currentPage, Integer pageSize, Long userId) {
        try {
            List<ImageBaseInfoDTO> baseInfoDTOS = imageInfoMapper.selectImageBaseInfoPage(currentPage, pageSize, userId);
            return CommonResult.success("success", JSON.toJSONString(baseInfoDTOS));
        } catch (Exception e) {
            log.error(e.toString());
            return CommonResult.failed(1, "获取图像基本信息失败", "[]");
        }
    }

    @Override
    public List<ImageInfo> getImageInfoPageByUserId(Integer currentPage, Integer pageSize, Long userId) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllPage(currentPage, pageSize, userId);
        return imageInfos;
    }

    /**
     * @param userId      用户id
     * @param imageUpload 上传的图像信息
     * @return
     */
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
                            .body(CommonResult.failed(1, "用户图像文件夹创建失败", e.getMessage()));
                }
            }
            Path fileSaveAbsolutePath = Paths.get(String.valueOf(thisUserImagePath), originalFilename);
            try {
                imageUpload.transferTo(fileSaveAbsolutePath);
                // 保存图像信息 同时对图像进行缩小化处理
                CommonResult<?> saveImageInfoResult = saveImageInfo(fileSaveAbsolutePath);
                if (saveImageInfoResult.getCode() <= 3) {
                    ImageInfo imageInfo = (ImageInfo) saveImageInfoResult.getData();
                    if (imageInfoMapper.isExistUserImageRelation(userId, imageInfo.getId()) > 0) {
                        return ResponseEntity.internalServerError().body(
                                CommonResult.failed(2, "已经存在对应的图像关系", null));
                    }
                    CommonResult<?> commonResult = saveUserImageRelation(imageInfo, userId);
                    if (commonResult.isSuccess()) {
                        return ResponseEntity.ok(CommonResult.success("文件上传完成 关系绑定完成", format));
                    } else return ResponseEntity.internalServerError().body(
                            CommonResult.failed(3, "用户图像的关系上传失败", null));
                } else return ResponseEntity.internalServerError().body(saveImageInfoResult);
            } catch (IOException e) {
                log.error(e.getMessage());
                return ResponseEntity.internalServerError()
                        .body(CommonResult.failed(4, "图像保存失败", e.getMessage()));
            }
        }
    }

    /**
     * @param md5 md5
     * @return
     */
    @Override
    public CommonResult<?> getImageInfoByMd5(String md5) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllByImageMd5(md5);
        if (imageInfos.size() == 0) return CommonResult.failed(1, "没有找到对应的图片", "");
        else return CommonResult.success("图像信息获取完成", JSON.toJSONString(imageInfos.get(0)));
    }

    @Override
    public CommonResult<?> saveImageInfo(Path image) {
        try {
            InputStream imageInputStream = Files.newInputStream(image);
            String fileMD5 = MD5.create().digestHex(imageInputStream);
            List<ImageInfo> imageInfos = imageInfoMapper.selectAllByImageMd5(fileMD5);
            if (imageInfos.size() > 0) {
                CommonResult<?> resultImgFix = imageFixSmallById(imageInfos.get(0).getId());
                if (!resultImgFix.isSuccess()) return CommonResult.failed(2, "图像缩小化修正失败", resultImgFix);
                return CommonResult.failed(1, "图像已经存在", imageInfos.get(0));
            }
            imageInputStream.close();
            imageInputStream = Files.newInputStream(image);
            Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
            HashMap<String, String> map = new HashMap<>();
            Set<String> set = new HashSet<>();
            set.add("Detected File Type Name");
            set.add("Image Height");
            set.add("Image Width");
            map.put("imageName", String.valueOf(image.getFileName()));
            map.put("imagePath", image.toString().substring(dataPath.length()));
            map.put("imageMd5", fileMD5);

            boolean isHeightSaved = false;
            boolean isWidthSaved = false;

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (set.contains(tag.getTagName())) {
                        if (tag.getTagName().equals("Image Height") && !isHeightSaved) {
                            log.info("{}:{}", tag.getTagName(), tag.getDescription());
                            map.put(tag.getTagName(), tag.getDescription().split(" ")[0]);
                            isHeightSaved = true;
                        } else if (tag.getTagName().equals("Image Width") && !isWidthSaved) {
                            log.info("{}:{}", tag.getTagName(), tag.getDescription());
                            map.put(tag.getTagName(), tag.getDescription().split(" ")[0]);
                            isWidthSaved = true;
                        } else {
                            String tagName = tag.getTagName();  //标签名
                            String desc = tag.getDescription(); //标签信息
                            if (tagName.equals("Image Height") || tagName.equals("Image Width"))
                                continue;
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
            CommonResult<?> resultImgFix = imageFixSmallById(imageInfo.getId());
            if (!resultImgFix.isSuccess()) return CommonResult.failed(3, "新保存图像缩小化修正失败", resultImgFix);
            return CommonResult.success("图像保存成功", imageInfo);
        } catch (ImageProcessingException | IOException e) {
            log.error(e.getMessage());
            return CommonResult.failed(4, "图像保存失败", e.getMessage());
        }
    }

    @Override
    public CommonResult<?> saveImageInfo(Path image, boolean isFixSmall) {
        try {
            InputStream imageInputStream = Files.newInputStream(image);
            String fileMD5 = MD5.create().digestHex(imageInputStream);
            List<ImageInfo> imageInfos = imageInfoMapper.selectAllByImageMd5(fileMD5);
            if (imageInfos.size() > 0) {
                CommonResult<?> resultImgFix = imageFixSmallById(imageInfos.get(0).getId());
                if (!resultImgFix.isSuccess()) return CommonResult.failed(2, "图像缩小化修正失败", resultImgFix);
                return CommonResult.failed(1, "图像已经存在", imageInfos.get(0));
            }
            imageInputStream.close();
            imageInputStream = Files.newInputStream(image);
            Metadata metadata = ImageMetadataReader.readMetadata(imageInputStream);
            HashMap<String, String> map = new HashMap<>();
            Set<String> set = new HashSet<>();
            set.add("Detected File Type Name");
            set.add("Image Height");
            set.add("Image Width");
            map.put("imageName", String.valueOf(image.getFileName()));
            map.put("imagePath", image.toString().substring(dataPath.length()));
            map.put("imageMd5", fileMD5);

            boolean isHeightSaved = false;
            boolean isWidthSaved = false;

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (set.contains(tag.getTagName())) {
                        if (tag.getTagName().equals("Image Height") && !isHeightSaved) {
                            log.info("{}:{}", tag.getTagName(), tag.getDescription());
                            map.put(tag.getTagName(), tag.getDescription().split(" ")[0]);
                            isHeightSaved = true;
                        } else if (tag.getTagName().equals("Image Width") && !isWidthSaved) {
                            log.info("{}:{}", tag.getTagName(), tag.getDescription());
                            map.put(tag.getTagName(), tag.getDescription().split(" ")[0]);
                            isWidthSaved = true;
                        } else {
                            String tagName = tag.getTagName();  //标签名
                            String desc = tag.getDescription(); //标签信息
                            if (tagName.equals("Image Height") || tagName.equals("Image Width"))
                                continue;
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
            if (isFixSmall) {
                CommonResult<?> resultImgFix = imageFixSmallById(imageInfo.getId());
                if (!resultImgFix.isSuccess()) return CommonResult.failed(3, "新保存图像缩小化修正失败", resultImgFix);
                return CommonResult.success("图像保存成功", imageInfo);
            } else {
                return CommonResult.success("图像保存成功，未进行缩小化处理", imageInfo);
            }
        } catch (ImageProcessingException | IOException e) {
            log.error(e.getMessage());
            return CommonResult.failed(4, "图像保存失败", e.getMessage());
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

    @Override
    public CommonResult<?> imageRestoreById(Long id) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectById(id);
        log.info("{}", imageInfos);
        if (imageInfos.size() == 0) {
            return CommonResult.failed(1, "图像修复-没有找到id对应的图片", "");
        } else {
            ImageInfo imageInfo = imageInfos.get(0);
            boolean b = restoreProducer.sendImageInfoForRestore(imageInfo);
            if (b) return CommonResult.success("图像修复-图像信息上传成功", "");
            else return CommonResult.failed(2, "图像修复-发送信息失败", "");
        }
    }

    @Override
    public CommonResult<?> countByUserId(Long id) {
        try {
            int i = imageInfoMapper.countByUserId(id);
            return CommonResult.success(i);
        } catch (Exception e) {
            return CommonResult.failed(1, "服务器出错", e.getMessage());
        }
    }

    @Override
    public CommonResult<?> imageFixSmallById(Long id) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectById(id);
        if (imageInfos.size() == 0) {
            return CommonResult.failed(1, "没有找到id对应的图片", "");
        } else {
            ImageInfo imageInfo = imageInfos.get(0);
            Long smallImgId = imageInfoMapper.selectSmallByOrigin(imageInfo.getId());
            if (smallImgId != null) return CommonResult.success("图像对应信息已经存在", "");
            boolean b = restoreProducer.sendImageInfoForResize(imageInfo);
            if (b) return CommonResult.success("图像信息上传成功", "");
            else return CommonResult.failed(2, "发送信息失败", "");
        }
    }

    @Override
    public CommonResult<?> insertOriginSmallRelation(Long originImageId, Long smallImageId) {
        int i = imageInfoMapper.insertOriginSmallRelation(originImageId, smallImageId);
        if (i > 0) return CommonResult.success("原图像与缩略图关系保存成功");
        return CommonResult.failed(1, "原图像与缩略图关系保存失败", null);
    }

    @Override
    public CommonResult<?> deleteImageInfoById(Long imageId) {
        int i = imageInfoMapper.deleteUserImageRelaByImageInfoId(imageId);
        if (i > 0) {
            int i1 = imageInfoMapper.deleteById(imageId);
            if (i1 > 0) return CommonResult.success("删除完成", null);
            else return CommonResult.failed(1, "删除图像信息失败", null);
        } else
            return CommonResult.failed(2, "删除图像用户联系信息失败", null);
    }

    /**
     * @param imageId
     * @return
     */
    @Override
    public CommonResult<?> imageColorizeById(Long imageId) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectById(imageId);
        if (imageInfos.size() == 0) {
            return CommonResult.failed(1, "图像上色-没有找到id对应的图片", "");
        } else {
            ImageInfo imageInfo = imageInfos.get(0);
            boolean b = restoreProducer.sendImageInfoForColorize(imageInfo);
            if (b) return CommonResult.success("图像上色-图像信息上传成功", "");
            else return CommonResult.failed(2, "图像上色-发送信息失败", "");
        }
    }
}
