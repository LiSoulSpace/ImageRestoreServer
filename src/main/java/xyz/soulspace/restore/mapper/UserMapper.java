package xyz.soulspace.restore.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
import xyz.soulspace.restore.dto.UserBasicDTO;
import xyz.soulspace.restore.entity.Role;
import xyz.soulspace.restore.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 03:46:51
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 通过username获取用户全部信息
     * @param username 用户名字
     * @return
     */
    List<User> selectAllByUsername(@Param("username") String username);

    /**
     * 通过id获取数量。。。
     * @param id id
     * @return int
     */
    int countById(@Param("id") Long id);

    /**
     * 通过id更新昵称和头像id
     * @param nickname 昵称
     * @param avatarImageId 头像id
     * @param id 用户id
     * @return int
     */
    int updateNicknameOrAvatarImageIdById(@Param("nickname") String nickname, @Param("avatarImageId") Long avatarImageId, @Param("id") Long id);

    /**
     * 通过用户名获取id 昵称 头像链接等主要用户信息
     * @param username 用户名字
     * @return {@link UserBasicDTO}
     */
    UserBasicDTO selectIdAndNicknameAndAvatarImageUriByUsername(@Param("username") String username);


    Role selectRoleById(@Param("id")Long id);
}
