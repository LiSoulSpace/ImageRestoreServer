package xyz.soulspace.restore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageTagRelation;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:05:18
 */
public interface ImageTagRelationService extends IService<ImageTagRelation> {
    CommonResult<?> getTagsByImageMd5(String md5);

    CommonResult<?> getImageBaseInfoByTagPage(List<String> tag,
                                              Integer currentPage,
                                              Integer pageSize,
                                              Integer userId);

    CommonResult<?> deleteTagImageRelation(Long tagId, Long imageId);
}
