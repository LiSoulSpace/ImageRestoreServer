package xyz.soulspace.restore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.soulspace.restore.dto.ImageBaseInfoDTO;
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
     *
     * @param tagId   tagId
     * @param imageId imageId
     * @return {@link int}
     */
    int insertTagImageRelation(@Param("tagId") Long tagId,
                               @Param("imageId") Long imageId);

    /**
     * 插入 通过tagName和userId
     *
     * @param tagName tagName
     * @param userId  userId
     * @return int
     */
    int insertByTagNameAndUserId(@Param("tagName") String tagName,
                                 @Param("userId") Long userId);

    /**
     * 通过多标签筛选，获取图像信息
     *
     * @param tags 标签列表
     * @return {@link List} {@link ImageInfo}
     */
    List<ImageInfo> findImageByTag(@Param("tags") List<Long> tags);

    /**
     * 通过多标签筛选，获取图像信息增加分页
     * @param tag 标签列表
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return
     */
    List<ImageInfo> findImageByTagPage(@Param("tags") List<Long> tag,
                                       @Param("currentPage") Integer currentPage,
                                       @Param("pageSize") Integer pageSize);

    /**
     * 通过多标签筛选，获取图像基本信息   增加分页
     * @param tag 标签
     * @param currentPage 当前页码
     * @param pageSize 每页数量
     * @param userId 用户id
     * @return
     */
    List<ImageBaseInfoDTO> selectImageBaseInfoByTagPage(@Param("tags") List<Long> tag,
                                                        @Param("currentPage") Integer currentPage,
                                                        @Param("pageSize") Integer pageSize,
                                                        @Param("userId") Integer userId);

    /**
     * 获取所有标签
     *
     * @return {@link List<Tag>}
     */
    List<Tag> selectAll();

    List<Tag> selectAllPage(@Param("currentPage") Integer currentPage,
                            @Param("pageSize") Integer pageSize,
                            @Param("creatorId") Long creatorId);

    List<Tag> findAllByTagNames(@Param("tagNames") List<String> tagNames);

    Integer countByTagCreatorId(@Param("creatorId") Long creatorId);

    /**
     * 获取主要标签，用于固定的选项显示
     *
     * @return {@link List} {@link Tag}
     */
    List<Tag> getMainTags();

    /**
     * 获取公共标签
     *
     * @return {@link List} {@link Tag}
     */
    List<Tag> getPublicTags();

    /**
     * 根据标签获取图像数量
     * @param tagsLong 标签id列表
     * @return
     */
    Integer countImageByTag(List<Long> tags);

    /**
     * 获取全部标签信息
     * @return
     */
    List<Tag> getAllTags();
}
