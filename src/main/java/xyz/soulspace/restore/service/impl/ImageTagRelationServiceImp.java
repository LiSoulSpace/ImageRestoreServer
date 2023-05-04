package xyz.soulspace.restore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.dto.ImageBaseInfoDTO;
import xyz.soulspace.restore.entity.ImageTagRelation;
import xyz.soulspace.restore.entity.Tag;
import xyz.soulspace.restore.mapper.ImageTagRelationMapper;
import xyz.soulspace.restore.mapper.TagMapper;
import xyz.soulspace.restore.service.ImageTagRelationService;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:05:18
 */
@Service
public class ImageTagRelationServiceImp extends ServiceImpl<ImageTagRelationMapper, ImageTagRelation> implements ImageTagRelationService {
    @Autowired
    ImageTagRelationMapper imageTagRelationMapper;
    @Autowired
    TagMapper tagMapper;

    /**
     * @param md5
     * @return
     */
    @Override
    public CommonResult<?> getTagsByImageMd5(String md5) {
        try {
            List<Tag> tags = imageTagRelationMapper.getTagsByImageMd5(md5);
            return CommonResult.success(JSON.toJSONString(tags));
        } catch (Exception e) {
            return CommonResult.failed(1, "根据md5获取标签失败", "");
        }
    }

    /**
     * @param tag
     * @param currentPage
     * @param pageSize
     * @param userId
     * @return
     */
    @Override
    public CommonResult<?> getImageBaseInfoByTagPage(List<String> tag, Integer currentPage, Integer pageSize, Integer userId) {
        try {
            List<Long> tagsLong = tagMapper.findAllByTagNames(tag).stream().map(Tag::getId).toList();
            List<ImageBaseInfoDTO> baseInfoDTOS = tagMapper.selectImageBaseInfoByTagPage(tagsLong, currentPage, pageSize, userId);
            return CommonResult.success(JSON.toJSONString(baseInfoDTOS));
        } catch (Exception e) {
            return CommonResult.failed(1, "分页获取图像基本信息失败", e.toString());
        }
    }

    /**
     * @param tagId
     * @param imageId
     * @return
     */
    @Override
    public CommonResult<?> deleteTagImageRelation(Long tagId, Long imageId) {
        try {
            Integer i = imageTagRelationMapper.deleteTagImageRelation(tagId, imageId);
            if (i > 0) return CommonResult.success(i);
            else return CommonResult.failed(2, "删除失败", i);
        } catch (Exception e) {
            return CommonResult.failed(1, "删除时出现问题", e.toString());
        }
    }
}
