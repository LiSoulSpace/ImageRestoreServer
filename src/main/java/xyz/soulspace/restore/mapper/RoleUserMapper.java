package xyz.soulspace.restore.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import org.apache.ibatis.annotations.Mapper;
import xyz.soulspace.restore.entity.Permission;
import xyz.soulspace.restore.entity.RoleUser;
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
public interface RoleUserMapper extends BaseMapper<RoleUser> {
    List<Permission> selectPermissionByUserId(@Param("userId") Long userId);
}
