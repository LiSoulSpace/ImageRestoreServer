package xyz.soulspace.restore.service;


import xyz.soulspace.restore.entity.Permission;

import java.util.List;

public interface UserCacheService {

    List<Permission> getPermissionList(Long userId);

    int setPermissionList(Long userId, List<Permission> permissionList);
}
