package xyz.soulspace.restore.service;

//import jakarta.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.dto.UserBasicDTO;
import xyz.soulspace.restore.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 03:46:51
 */
public interface UserService extends IService<User> {
    /**
     * 判断是否存在用户
     *
     * @param userId 用户id
     * @return boolean
     */
    boolean isExistUser(Long userId);

    /**
     * 更新用户信息
     *
     * @param nickName         昵称
     * @param avatar_md5       头像md5
     * @param userId           userId
     * @param contentType
     * @param fileRelativePath
     * @return
     */
    boolean updateUserInfo(String nickName, String avatar_md5, Long userId, String contentType, String fileRelativePath);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return Map
     */
    Map<String, String> login(String username, String password);

    String register(String username, String password);

    String logout(String username);

    UserBasicDTO getUserBasicDTOByUsername(String username);

    UserDetails loadUserByUsername(String username, String password);

    UserDetails loadUserByUsername(String username);

    User getByUserName(String username);

    /**
     * 通过token获取个人基本信息
     *
     * @param token token
     * @return {@link UserBasicDTO}
     */
    UserBasicDTO whoAmI(String token);

    /**
     * 通过Servlet request 获取用户基本信息
     *
     * @param request {@link HttpServletRequest}
     * @return {@link UserBasicDTO}
     */
    UserBasicDTO whoAmI(HttpServletRequest request);

    /**
     * 通过id获取用户对应的角色信息
     *
     * @param userId 用户id
     * @return {@link CommonResult}
     */
    CommonResult<?> selectRoleByUserId(Long userId);

    /**
     * 通过userId 判断是否为管理员
     * @param userId 用户id
     * @return {@link CommonResult}
     */
    CommonResult<?> checkIsAdminByUserId(Long userId);
}
