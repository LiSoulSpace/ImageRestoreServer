package xyz.soulspace.restore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.soulspace.restore.entity.ImageTagRelation;
import xyz.soulspace.restore.entity.Tag;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:05:18
 */
@Mapper
public interface ImageTagRelationMapper extends BaseMapper<ImageTagRelation> {
    List<Tag> getTagsByImageMd5(@Param("md5") String md5);

    Integer deleteTagImageRelation(@Param("tagId") Long tagId, @Param("imageId") Long imageId);
}
