package xyz.soulspace.restore.mapper;

import xyz.soulspace.restore.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 05:21:51
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

}
