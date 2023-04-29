package xyz.soulspace.restore.service;

import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.OriginSmallRelation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-04-26 10:31:06
 */
public interface OriginSmallRelationService extends IService<OriginSmallRelation> {

    /**
     * 获取图片的数量，通过原图与缩略图的关系表，去除重复内容
     * @param isPublic 是否为公共图像
     * @return
     */
    CommonResult<?> countByIsPublic(Byte isPublic);
}
