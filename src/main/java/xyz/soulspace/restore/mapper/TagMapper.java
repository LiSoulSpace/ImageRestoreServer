package xyz.soulspace.restore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.entity.Tag;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-03-27 04:00:28
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    int insertTagImageRelation(@Param("tagId") Long tagId,
                               @Param("imageId") Long imageId);

    int insertByTagNameAndUserId(@Param("tagName") String tagName,
                                 @Param("userId") Long userId);

    List<ImageInfo> findImageByTag(@Param("tags") List<Long> tags);

    List<Tag> selectAll();

    List<Tag> selectAllPage(@Param("currentPage") Integer currentPage,
                            @Param("pageSize") Integer pageSize,
                            @Param("creatorId") Long creatorId);

    List<Tag> findAllByTagNames(@Param("tagNames") List<String> tagNames);

    Integer countByTagCreatorId(@Param("creatorId") Long creatorId);
}
