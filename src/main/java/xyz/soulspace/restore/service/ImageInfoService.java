package xyz.soulspace.restore.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.nio.file.Path;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 07:04:23
 */
public interface ImageInfoService extends IService<ImageInfo> {
    List<String> getImagePathPage(Integer currentPage, Integer pageSize);

    List<ImageInfo> getImageInfoPage(Integer currentPage, Integer pageSize);

    List<ImageInfo> getImageInfoPageByUserId(Integer currentPage, Integer pageSize, Long userId);

    ResponseEntity<?> uploadImageByUserId(Long userId, MultipartFile imageUpload);

    CommonResult<?> saveImageInfo(Path imagePath);

    CommonResult<?> saveUserImageRelation(ImageInfo savedImageInfo, Long userId);

    CommonResult<?> getImagePathById(Long id);

    CommonResult<?> imageRestoreById(Long id);

    CommonResult<?> countByUserId(Long id);
}
