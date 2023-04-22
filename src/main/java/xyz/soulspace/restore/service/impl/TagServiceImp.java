package xyz.soulspace.restore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.entity.Tag;
import xyz.soulspace.restore.mapper.TagMapper;
import xyz.soulspace.restore.service.TagService;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-03-27 04:00:28
 */
@Service
public class TagServiceImp extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    TagMapper tagMapper;

    /**
     * @param tagId   标签id
     * @param imageId 图像id
     * @return
     */
    @Override
    public CommonResult<?> saveTagImageRelation(Long tagId, Long imageId) {
        int i = tagMapper.insertTagImageRelation(tagId, imageId);
        if (i > 0) return CommonResult.success("标签图像关系保存成功", null);
        return CommonResult.failed(1, "标签图像关系保存失败", null);
    }

    /**
     * @param tag    标签名称
     * @param userId 用户id
     * @return
     */
    @Override
    public CommonResult<?> saveTag(String tag, Long userId) {
        int i = tagMapper.insertByTagNameAndUserId(tag, userId);
        if (i > 0) return CommonResult.success("标签保存成功", null);
        return CommonResult.failed(1, "标签保存失败", null);
    }

    /**
     * @param tags
     * @return
     */
    @Override
    public CommonResult<?> findImageByTags(List<String> tags) {
        try {
            List<Long> tagsLong = tagMapper.findAllByTagNames(tags).stream().map(Tag::getId).toList();
            List<ImageInfo> image = tagMapper.findImageByTag(tagsLong);
            return CommonResult.success("图像信息寻找完成", image);
        } catch (Exception e) {
            return CommonResult.failed(2, "图像信息寻找失败", e.getMessage());
        }
    }

    /**
     * @param currentPage 当前页数
     * @param pageSize    每页数量
     * @return
     */
    @Override
    public CommonResult<?> getTagsByCreatorIdPage(Integer currentPage, Integer pageSize, Long creatorId) {
        try {
            List<Tag> tags = tagMapper.selectAllPage(currentPage, pageSize, creatorId);
            return CommonResult.success("标签分页获取完成", JSON.toJSONString(tags));
        } catch (Exception e) {
            return CommonResult.failed(1, "标签获取错误", e.toString());
        }
    }

    /**
     * @param creatorId 标签创建者id
     * @return 1:获取失败
     */
    @Override
    public CommonResult<?> countByCreatorId(Long creatorId) {
        try {
            Integer integer = tagMapper.countByTagCreatorId(creatorId);
            return CommonResult.success("", integer);
        } catch (Exception e) {
            return CommonResult.failed(1, "", e.getMessage());
        }
    }


    /**
     * @return 1:获取失败
     */
    @Override
    public CommonResult<?> getMainTags() {
        try {
            List<Tag> mainTags = tagMapper.getMainTags();
            return CommonResult.success("主要标签", mainTags);
        } catch (Exception e) {
            return CommonResult.failed(1, "主要标签获取失败", e.getMessage());
        }
    }

    /**
     * @return 1:获取失败
     */
    @Override
    public CommonResult<?> getPublicTags() {
        try {
            List<Tag> publicTags = tagMapper.getPublicTags();
            return CommonResult.success("公共标签", publicTags);
        } catch (Exception e) {
            return CommonResult.failed(1, "公共标签获取失败", e.getMessage());
        }
    }
}
