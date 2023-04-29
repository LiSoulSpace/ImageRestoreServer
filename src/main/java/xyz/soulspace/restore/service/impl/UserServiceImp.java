package xyz.soulspace.restore.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.api.CommonResult;
import xyz.soulspace.restore.component.security.AppUserDetails;
import xyz.soulspace.restore.dto.UserBasicDTO;
import xyz.soulspace.restore.entity.ImageInfo;
import xyz.soulspace.restore.entity.Permission;
import xyz.soulspace.restore.entity.Role;
import xyz.soulspace.restore.entity.User;
import xyz.soulspace.restore.mapper.ImageInfoMapper;
import xyz.soulspace.restore.mapper.RoleUserMapper;
import xyz.soulspace.restore.mapper.UserMapper;
import xyz.soulspace.restore.redis.RedisService;
import xyz.soulspace.restore.service.UserCacheService;
import xyz.soulspace.restore.service.UserService;
import xyz.soulspace.restore.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 03:46:51
 */
@Service
@Slf4j
public class UserServiceImp extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    RedisService redisService;
    @Autowired
    UserCacheService userCacheService;
    @Autowired
    RoleUserMapper roleUserMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Autowired
    ImageInfoMapper imageInfoMapper;

    @Override
    public boolean isExistUser(Long userId) {
        int count = userMapper.countById(userId);
        return count > 0;
    }

    @Override
    public boolean updateUserInfo(String nickName, String avatar_md5,
                                  Long userId, String contentType,
                                  String fileRelativePath) {
        List<ImageInfo> imageInfos = imageInfoMapper.selectAllByImageMd5(avatar_md5);
        String encode = Base64.encode(String.valueOf(userId));
        if (imageInfos.size() == 0) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setImageMd5(avatar_md5);
            imageInfo.setImagePath(fileRelativePath);
            imageInfo.setImageName(encode);
            imageInfo.setImageType(contentType);
            int insert = imageInfoMapper.insert(imageInfo);
            if (insert > 0) {
                int i = userMapper.updateNicknameOrAvatarImageIdById(nickName, imageInfo.getId(), userId);
                return i > 0;
            } else {
                return false;
            }
        } else {
            int i1 = imageInfoMapper.updateImageMd5ByImageName(avatar_md5, encode);
            List<ImageInfo> imageInfoList = imageInfoMapper.selectAllByImageMd5(avatar_md5);
            if (i1 > 0) {
                int i = userMapper.updateNicknameOrAvatarImageIdById(nickName,
                        imageInfoList.get(0).getId(), userId);
                return i > 0;
            } else return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username, String password) {
        List<User> users = userMapper.selectAllByUsername(username);
        if (users.size() == 0) {
            register(username, password);
        }
        users = userMapper.selectAllByUsername(username);
        log.warn(users.toString());
        if (users.size() > 0) {
            List<Permission> permissionList = getPermissionList(users.get(0).getId());
            log.warn(String.valueOf(users.get(0)));
            return new AppUserDetails(users.get(0), permissionList);
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
    }

    public List<Permission> getPermissionList(Long userId) {
        List<Permission> resourceList = userCacheService.getPermissionList(userId);
        if (CollUtil.isNotEmpty(resourceList)) {
            return resourceList;
        }
        resourceList = roleUserMapper.selectPermissionByUserId(userId);
        if (CollUtil.isNotEmpty(resourceList)) {
            userCacheService.setPermissionList(userId, resourceList);
        }
        return resourceList;
    }

    @Override
    public Map<String, String> login(String username, String password) {
        String token = null;
        try {
            Map<String, String> resultMap = new HashMap<>();
            UserDetails userDetails = loadUserByUsername(username, password);
            log.warn("password {} , userDetailsPassword {}", password, userDetails.getPassword());
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                resultMap.put("msg", "用户名或密码错误");
                return resultMap;
            }
            if (!userDetails.isEnabled()) {
                resultMap.put("msg", "账号被禁用");
                return resultMap;
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);

            UserBasicDTO userBasicDTOByUsername = getUserBasicDTOByUsername(username);
            log.warn("{}", userBasicDTOByUsername);
            resultMap.put("userId", String.valueOf(userBasicDTOByUsername.getUserId()));
            resultMap.put("nickName", userBasicDTOByUsername.getNickName());
            resultMap.put("avatar", userBasicDTOByUsername.getAvatarUri());
            resultMap.put("token", token);
            return resultMap;
        } catch (AuthenticationException e) {
            log.warn("登录异常:{}", e.getMessage());
        }
        return new HashMap<>() {{
            put("token", null);
        }};
    }

    @Override
    public String register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(username);
        user.setAvatarImageId(1L);
        user.setDescription(username);
        int insert = userMapper.insert(user);
        if (insert > 0) return username;
        else return null;
    }

    @Override
    public String logout(String username) {
        return null;
    }

    @Override
    public UserBasicDTO getUserBasicDTOByUsername(String username) {
        return userMapper.selectIdAndNicknameAndAvatarImageUriByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User userName = getByUserName(username);
        if (userName != null) {
            return new AppUserDetails(userName, new ArrayList<>());
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public User getByUserName(String username) {
        List<User> users = userMapper.selectAllByUsername(username);
        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public UserBasicDTO whoAmI(String token) {
        token = token.substring(tokenHead.length());
        String username = jwtTokenUtil.getUserNameFromToken(token);
        return userMapper.selectIdAndNicknameAndAvatarImageUriByUsername(username);
    }

    @Override
    public UserBasicDTO whoAmI(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        return whoAmI(token);
    }

    /**
     * @param userId 用户id
     * @return {@link CommonResult} 0-成功 1-获取失败
     */
    @Override
    public CommonResult<?> selectRoleByUserId(Long userId) {
        try {
            Role role = userMapper.selectRoleById(userId);
            return CommonResult.success(JSON.toJSONString(role));
        } catch (Exception e) {
            return CommonResult.failed(1, "获取用户对应的角色信息失败", e.toString());
        }
    }

    /**
     * @param userId 用户id
     * @return {@link CommonResult}
     */
    @Override
    public CommonResult<?> checkIsAdminByUserId(Long userId) {
        try {
            Role role = userMapper.selectRoleById(userId);
            if (role.getRole().equals("ADMIN")) {
                return CommonResult.success(1);
            } else return CommonResult.success(0);
        } catch (Exception e) {
            return CommonResult.failed(1, "判断管理员身份时出现问题", e.toString());
        }
    }
}
