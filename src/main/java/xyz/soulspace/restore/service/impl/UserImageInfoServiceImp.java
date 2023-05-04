package xyz.soulspace.restore.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.UserImageInfo;
import xyz.soulspace.restore.mapper.UserImageInfoMapper;
import xyz.soulspace.restore.service.UserImageInfoService;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:36:00
 */
@Service
public class UserImageInfoServiceImp extends ServiceImpl<UserImageInfoMapper, UserImageInfo> implements UserImageInfoService {

    @Autowired
    UserImageInfoMapper userImageInfoMapper;

    /**
     * @param imageMd5 图像MD5
     * @param userId   userId
     * @return
     */
    @Override
    public CommonResult<?> checkImageUserRela(String imageMd5, Long userId) {
        try {
            List<UserImageInfo> userImageInfos = userImageInfoMapper.selectAllByImageMd5AndUserId(imageMd5, userId);
            if (userImageInfos.isEmpty()) {
                return CommonResult.success("{\"isExist\":0}");
            } else return CommonResult.success("{\"isExist\":1}");
        } catch (Exception e) {
            return CommonResult.failed(1, "判断用户图像关系失败", "");
        }


    }

    /**
     * @param imageMd5 图像md5
     * @param userId   userId
     * @param isSet    绑定关系-1 解除关系-0
     * @return
     */
    @Override
    public CommonResult<?> setImageUserRela(String imageMd5, Long userId, Integer isSet) {
        try {
            if (isSet == 1) {
                Integer integer = userImageInfoMapper.insertAllByImageMd5AndUserId(imageMd5, userId);
                if (integer > 0) return CommonResult.success("用户图像关系添加成功", integer);
                else return CommonResult.failed(3, "插入用户图像信息失败", integer);
            } else if (isSet == 0) {
                List<UserImageInfo> userImageInfos = userImageInfoMapper.selectAllByImageMd5AndUserId(imageMd5, userId);
                if (userImageInfos.isEmpty())
                    return CommonResult.failed(2, "用户图像关系信息不存在", "");
                else {
                    int i = userImageInfoMapper.deleteById(userImageInfos.get(0).getId());
                    if (i > 0) return CommonResult.success("删除用户图像关系信息", "");
                    else return CommonResult.failed(4, "删除用户图像信息失败", "");
                }
            } else {
                return CommonResult.failed(5, "未知的指令", "");
            }
        } catch (Exception e) {
            return CommonResult.failed(1, "设置用户图像关系失败", e.toString());
        }
    }
}
