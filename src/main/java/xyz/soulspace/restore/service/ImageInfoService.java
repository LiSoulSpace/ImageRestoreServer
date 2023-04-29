package xyz.soulspace.restore.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.dto.ImageBaseInfoDTO;
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
     * 分页获取图像全部信息
     * @param currentPage 当前页数
     * @param pageSize 每页大小
     * @return 图像信息列表
     */
    List<ImageInfo> getImageInfoPage(Integer currentPage, Integer pageSize);

    /**
     * 获取图像信息的全部数量 (数据表中的数据数量，用于管理员的处理)
     * @return {@link Integer}
     */
    CommonResult<?> getImageInfoCount();

    /**
     * 分页获取公开图像基本信息 (通过图像原图与缩略图的关系表)
     * @param currentPage 当前页码
     * @param pageSize 每页的数量
     * @return
     */
    CommonResult<?> getImageBaseInfoPage(Integer currentPage, Integer pageSize);

    /**
     * 分页获取用户的图像基本信息 (通过图像原图与缩略图的关系表)
     * @param currentPage 当前页码
     * @param pageSize 每页的数量
     * @param userId 用户id
     * @return
     */
    CommonResult<?> getImageBaseInfoPage(Integer currentPage, Integer pageSize, Long userId);

    List<ImageInfo> getImageInfoPageByUserId(Integer currentPage, Integer pageSize, Long userId);

    /**
     * 上传图片，并绑定与用户的关系
     * @param userId 用户id
     * @param imageUpload 上传的图像信息
     * @return
     */
    ResponseEntity<?> uploadImageByUserId(Long userId, MultipartFile imageUpload);

    /**
     * 通过md5获取图像信息
     * @param md5 md5
     * @return {@link CommonResult}
     */
    CommonResult<?> getImageInfoByMd5(String md5);

    /**
     * 根据路径保存图像信息(默认对图像进行缩小化处理)
     * @param image 图像的路径 {@link Path}
     * @return
     */
    CommonResult<?> saveImageInfo(Path image);

    /**
     * 根据路径保存图像信息(后方的第二个参数可以进行设置是否需要对图像进行缩小化处理)
     * @param image 图像的路径 {@link Path}
     * @param isFixSmall 是否需要对图像进行缩小化处理
     * @return
     */
    CommonResult<?> saveImageInfo(Path image, boolean isFixSmall);

    /**
     * 保存用户图像信息关系
     * @param savedImageInfo {@link ImageInfo}
     * @param userId 用户id
     * @return
     */
    CommonResult<?> saveUserImageRelation(ImageInfo savedImageInfo, Long userId);

    /**
     * 通过id获取图像路径
     * @param id Image id
     * @return
     */
    CommonResult<?> getImagePathById(Long id);

    /**
     * 通过图像id对图像进行修复
     * TODO: 图像修复在Service中还没有处理！！！！
     * @param id 图像id
     * @return
     */
    CommonResult<?> imageRestoreById(Long id);

    /**
     * 通过用户id 查询用户对应的图像数量
     * @param id userId
     * @return
     */
    CommonResult<?> countByUserId(Long id);

    /**
     * 通过图像id进行图像缩小化
     * @param id 图像id
     * @return
     */
    CommonResult<?> imageFixSmallById(Long id);

    CommonResult<?> insertOriginSmallRelation(Long originImageId, Long smallImageId);

    CommonResult<?> deleteImageInfoById(Long imageId);

    CommonResult<?> imageColorizeById(Long imageId);
}
