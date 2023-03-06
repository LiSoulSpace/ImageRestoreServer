package xyz.soulspace.restore.service.impl;

import xyz.soulspace.restore.entity.Test;
import xyz.soulspace.restore.mapper.TestMapper;
import xyz.soulspace.restore.service.TestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author soulspace
 * @since 2023-02-28 10:26:01
 */
@Service
public class TestServiceImp extends ServiceImpl<TestMapper, Test> implements TestService {

}
