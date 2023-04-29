package xyz.soulspace.restore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.OriginSmallRelation;
import xyz.soulspace.restore.mapper.OriginSmallRelationMapper;
import xyz.soulspace.restore.service.OriginSmallRelationService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-04-26 10:31:06
 */
@Service
public class OriginSmallRelationServiceImp extends ServiceImpl<OriginSmallRelationMapper, OriginSmallRelation> implements OriginSmallRelationService {

    @Autowired
    OriginSmallRelationMapper originSmallRelationMapper;

    public  CommonResult<?> countByIsPublic(Byte isPublic) {
        try {
            Integer integer = originSmallRelationMapper.countByIsPublic(isPublic);
            return CommonResult.success(integer);
        } catch (Exception e) {
            return CommonResult.failed(1, "获取公共图像信息失败", "[]");
        }
    }
}
