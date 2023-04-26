package xyz.soulspace.restore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.soulspace.restore.dto.ImageBaseInfoDTO;
import xyz.soulspace.restore.entity.ImageInfo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 07:04:23
 */
@Mapper
public interface ImageInfoMapper extends BaseMapper<ImageInfo> {
    List<ImageInfo> selectById(@Param("id") Long id);

    List<ImageInfo> selectImagePathById(@Param("id") Long id);

    List<ImageInfo> selectAllByImageMd5(@Param("imageMd5") String imageMd5);

    int updateImageMd5ByImageName(@Param("imageMd5") String imageMd5, @Param("imageName") String imageName);

    /**
     * 获取图像的全部信息
     * @param currentPage 当前页
     * @param pageSize 每页的信息数量
     * @param userId 用户id 如果查询公共图像 则为null
     * @return {@link ImageInfo}
     */
    List<ImageInfo> selectAllPage(@Param("currentPage") Integer currentPage,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("userId") Long userId);

    /**
     * 获取图像的基本信息
     * @param currentPage 当前页
     * @param pageSize 每页的信息数量
     * @param userId 用户id 如果查询公共图像 则为null
     * @return {@link ImageBaseInfoDTO}
     */
    List<ImageBaseInfoDTO> selectImageBaseInfoPage(@Param("currentPage") Integer currentPage,
                                                   @Param("pageSize") Integer pageSize,
                                                   @Param("userId") Long userId);

    int insertUserImageInfo(@Param("userId") Long userId, @Param("imageInfoId") Long imageInfoId);

    List<ImageInfo> selectAllByUserId(@Param("id") Long userId);

    /**
     * 获取每个用户的图像数量
     * @param id userId
     * @return int
     */
    int countByUserId(@Param("id") Long id);

    /**
     * 插入原图与缩略图的关系
     * @param originId 原图id
     * @param small 缩略图id
     * @return {@link int}
     */
    int insertOriginSmallRelation(@Param("originId") Long originId,
                                  @Param("smallId") Long small);

    /**
     * 通过原图像获取缩略图id
     * @param originId 原图id
     * @return {@link Long}
     */
    Long selectSmallByOrigin(@Param("originId")Long originId);

    /**
     * 通过md5获取图像是否存在
     * @param imageMd5 图像的md5
     * @return int
     */
    int countByImageMd5(@Param("imageMd5") String imageMd5);

    /**
     * 判断是否存在用户图像关系
     * @param userId 用户id
     * @param imageInfoId 图像信息id
     * @return int
     */
    int isExistUserImageRelation(@Param("userId") Long userId,
                                 @Param("image_info_id") Long imageInfoId);

    int deleteUserImageRelaByImageInfoId(@Param("imageInfoId")Long imageInfoId);

    int deleteByImageMd5(@Param("imageMd5") String imageMd5);
}
