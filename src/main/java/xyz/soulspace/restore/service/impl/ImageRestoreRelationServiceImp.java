package xyz.soulspace.restore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.ImageRestoreRelation;
import xyz.soulspace.restore.mapper.ImageRestoreRelationMapper;
import xyz.soulspace.restore.service.ImageRestoreRelationService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-05-04 11:19:25
 */
@Service
public class ImageRestoreRelationServiceImp extends ServiceImpl<ImageRestoreRelationMapper, ImageRestoreRelation> implements ImageRestoreRelationService {

    @Autowired
    ImageRestoreRelationMapper imageRestoreRelationMapper;

    /**
     * @param originId
     * @param restoreId
     * @return
     */
    @Override
    public CommonResult<?> insertImageRestoreRe(Long originId, Long restoreId, String type) {
        ImageRestoreRelation imageRestoreRelation = new ImageRestoreRelation();
        imageRestoreRelation.setRestoreImageId(restoreId);
        imageRestoreRelation.setImageId(originId);
        imageRestoreRelation.setRestoreType(type);
        try {
            int insert = imageRestoreRelationMapper.insert(imageRestoreRelation);
            if (insert > 0)
                return CommonResult.success("原图修复图像关系插入成功", insert);
            else CommonResult.failed(2, "原图修复图像关系插入失败", insert);
        } catch (Exception e) {
            return CommonResult.failed(1, "原图修复图像关系插入失败", e.toString());
        }
        return CommonResult.failed(9, "未知错误", "");
    }
}
