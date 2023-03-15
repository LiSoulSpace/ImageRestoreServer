package xyz.soulspace.restore.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import xyz.soulspace.restore.entity.ImageInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

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

    List<ImageInfo> selectAllPage(@Param("currentPage") Integer currentPage,
                                  @Param("pageSize") Integer pageSize,
                                  @Param("userId") Long userId);

    int insertUserImageInfo(@Param("userId") Long userId, @Param("imageInfoId") Long imageInfoId);

    List<ImageInfo> selectAllByUserId(@Param("id") Long userId);

    int countByUserId(@Param("id") Long id);
}
