package xyz.soulspace.restore.service;

//import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import xyz.soulspace.restore.dto.UserBasicDTO;
import xyz.soulspace.restore.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 03:46:51
 */
public interface UserService extends IService<User> {
    boolean isExistUser(Long userId);

    boolean updateUserInfo(String nickName, String avatar_md5, Long userId, String contentType, String fileRelativePath);

    Map<String, String> login(String username, String password);

    String register(String username, String password);

    String logout(String username);

    UserBasicDTO getUserBasicDTOByUsername(String username);

    UserDetails loadUserByUsername(String username, String password);

    UserDetails loadUserByUsername(String username);

    User getByUserName(String username);

    UserBasicDTO whoAmI(String token);

    UserBasicDTO whoAmI(HttpServletRequest request);
}
