package xyz.soulspace.restore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.soulspace.restore.entity.UserImageInfo;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:36:00
 */
@Mapper
public interface UserImageInfoMapper extends BaseMapper<UserImageInfo> {
    List<UserImageInfo> selectAllByImageMd5AndUserId(@Param("imageMd5") String imageMd5,
                                                     @Param("userId") Long userId);

    Integer insertAllByImageMd5AndUserId(@Param("imageMd5") String imageMd5,
                                         @Param("userId") Long userId);

}
