package xyz.soulspace.restore.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import xyz.soulspace.restore.entity.ImageRestoreRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-05-04 11:19:25
 */
@Mapper
public interface ImageRestoreRelationMapper extends BaseMapper<ImageRestoreRelation> {
    /**
     * 根据原图id和修复方式 寻找修复结果
     * @param imageId 原图id
     * @param restoreType 修复方式
     * @return {@link List} {@link ImageRestoreRelation}
     */
    List<ImageRestoreRelation> selectByImageIdAndRestoreType(@Param("imageId") Long imageId, @Param("restoreType") String restoreType);
}
