package xyz.soulspace.restore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.Tag;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-03-27 04:00:28
 */
public interface TagService extends IService<Tag> {
    /**
     * 保存标签图像关系
     *
     * @param tagId   标签id
     * @param imageId 图像id
     * @return {@link CommonResult}
     */
    CommonResult<?> saveTagImageRelation(Long tagId, Long imageId);

    /**
     * 保存标签信息
     *
     * @param tag    标签名称
     * @param userId 用户id
     * @return {@link CommonResult}
     */
    CommonResult<?> saveTag(String tag, Long userId);

    /**
     * 通过标签查找图片
     *
     * @param tags 标签信息 String
     * @return {@link CommonResult}
     */
    CommonResult<?> findImageByTags(List<String> tags);

    /**
     * 分页获取标签
     *
     * @param currentPage 当前页数
     * @param pageSize    每页数量
     * @return {@link CommonResult}
     */
    CommonResult<?> getTagsByCreatorIdPage(Integer currentPage, Integer pageSize, Long creatorId);

    /**
     * 根据creatorId获取标签数量
     *
     * @param creatorId 标签创建者id
     * @return {@link CommonResult}
     */
    CommonResult<?> countByCreatorId(Long creatorId);

    /**
     * 获取主要标签
     *
     * @return {@link CommonResult}
     */
    CommonResult<?> getMainTags();

    /**
     * 获取公共标签
     *
     * @return {@link CommonResult}
     */
    CommonResult<?> getPublicTags();
}
