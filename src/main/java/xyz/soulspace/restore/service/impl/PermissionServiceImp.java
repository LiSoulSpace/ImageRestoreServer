package xyz.soulspace.restore.service.impl;

import xyz.soulspace.restore.entity.Permission;
import xyz.soulspace.restore.mapper.PermissionMapper;
import xyz.soulspace.restore.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色权限表 服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 05:21:51
 */
@Service
public class PermissionServiceImp extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
