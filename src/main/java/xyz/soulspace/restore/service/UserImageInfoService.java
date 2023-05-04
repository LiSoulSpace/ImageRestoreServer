package xyz.soulspace.restore.service;

import com.baomidou.mybatisplus.extension.service.IService;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.entity.UserImageInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-05-01 02:36:00
 */
public interface UserImageInfoService extends IService<UserImageInfo> {
    /**
     * 判断图像与用户的关系
     *
     * @param imageMd5 图像MD5
     * @param userId   userId
     * @return {@link CommonResult}
     */
    CommonResult<?> checkImageUserRela(String imageMd5, Long userId);

    /**
     * 设置图像与用户的关系
     *
     * @param imageMd5 图像md5
     * @param userId   userId
     * @param isSet    绑定关系-1 解除关系-0
     * @return {@link CommonResult}
     */
    CommonResult<?> setImageUserRela(String imageMd5, Long userId, Integer isSet);
}
