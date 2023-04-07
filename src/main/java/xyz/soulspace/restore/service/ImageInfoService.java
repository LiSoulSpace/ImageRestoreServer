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
    /**
     * 分页获取图像路径信息
     *
     * @param currentPage 当前页数
     * @param pageSize 每页大小
     * @return 图像路径
     */
    List<String> getImagePathPage(Integer currentPage, Integer pageSize);

    /**
     * 分页获取图像信息
     * @param currentPage 当前页数
     * @param pageSize 每页大小
     * @return 图像信息列表
     */
    List<ImageInfo> getImageInfoPage(Integer currentPage, Integer pageSize);

    List<ImageInfo> getImageInfoPageByUserId(Integer currentPage, Integer pageSize, Long userId);

    /**
     * 上传图片，并绑定与用户的关系
     * @param userId 用户id
     * @param imageUpload 上传的图像信息
     * @return
     */
    ResponseEntity<?> uploadImageByUserId(Long userId, MultipartFile imageUpload);

    CommonResult<?> saveImageInfo(Path imagePath);

    CommonResult<?> saveUserImageRelation(ImageInfo savedImageInfo, Long userId);

    CommonResult<?> getImagePathById(Long id);

    CommonResult<?> imageRestoreById(Long id);

    CommonResult<?> countByUserId(Long id);

    CommonResult<?> imageFixSmallById(Long id);

    CommonResult<?> insertOriginSmallRelation(Long originImageId, Long smallImageId);

    CommonResult<?> deleteImageInfoById(Long imageId);
}
