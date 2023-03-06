package xyz.soulspace.restore.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.soulspace.restore.entity.Permission;
import xyz.soulspace.restore.redis.RedisService;
import xyz.soulspace.restore.service.UserCacheService;


import java.util.List;

@Slf4j
@Service
public class UserCacheServiceImp implements UserCacheService {
    @Autowired
    RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.permissionList}")
    private String REDIS_KEY_RESOURCE_LIST;

    @Override
    public List<Permission> getPermissionList(Long userId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + userId;
        return (List<Permission>) redisService.get(key);
    }

    @Override
    public int setPermissionList(Long userId, List<Permission> permissionList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCE_LIST + ":" + userId;
        try {
            redisService.set(key, permissionList, REDIS_EXPIRE);
            return 1;
        }catch (Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }
}
