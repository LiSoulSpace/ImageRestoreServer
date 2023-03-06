package xyz.soulspace.restore.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
import xyz.soulspace.restore.dto.UserBasicDTO;
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
    List<User> selectAllByUsername(@Param("username") String username);

    int countById(@Param("id") Long id);

    int updateNicknameOrAvatarImageIdById(@Param("nickname") String nickname, @Param("avatarImageId") Long avatarImageId, @Param("id") Long id);

    UserBasicDTO selectIdAndNicknameAndAvatarImageUriByUsername(@Param("username") String username);
}
