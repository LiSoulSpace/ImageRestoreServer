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
    /**
     * 插入标签图像关系
     * @param tagId tagId
     * @param imageId imageId
     * @return {@link int}
     */
    int insertTagImageRelation(@Param("tagId") Long tagId,
                               @Param("imageId") Long imageId);

    /**
     * 插入 通过tagName和userId
     * @param tagName tagName
     * @param userId userId
     * @return int
     */
    int insertByTagNameAndUserId(@Param("tagName") String tagName,
                                 @Param("userId") Long userId);

    /**
     * 通过多标签筛选，获取图像信息
     * @param tags 标签列表
     * @return {@link List} {@link ImageInfo}
     */
    List<ImageInfo> findImageByTag(@Param("tags") List<Long> tags);

    /**
     * 获取所有标签
     * @return {@link List<Tag>}
     */
    List<Tag> selectAll();

    List<Tag> selectAllPage(@Param("currentPage") Integer currentPage,
                            @Param("pageSize") Integer pageSize,
                            @Param("creatorId") Long creatorId);

    List<Tag> findAllByTagNames(@Param("tagNames") List<String> tagNames);

    Integer countByTagCreatorId(@Param("creatorId") Long creatorId);
}
