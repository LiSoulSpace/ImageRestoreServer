package xyz.soulspace.restore.service;

import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageRestoreRelation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-05-04 11:19:25
 */
public interface ImageRestoreRelationService extends IService<ImageRestoreRelation> {
    CommonResult<?> insertImageRestoreRe(Long originId, Long restoreId, String type);
}
