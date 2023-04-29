package xyz.soulspace.restore.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.soulspace.restore.dto.ImageBaseInfoDTO;
import xyz.soulspace.restore.entity.OriginSmallRelation;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author soulspace
 * @since 2023-04-26 10:31:06
 */
@Mapper
public interface OriginSmallRelationMapper extends BaseMapper<OriginSmallRelation> {

    /**
     * 选取公开的图片关系信息
     *
     * @param isPublic 是否为公开图片 是-1 否-0
     * @return List {@link OriginSmallRelation}
     */
    List<OriginSmallRelation> selectAllByIsPublic(@Param("isPublic") Byte isPublic);

    /**
     * 根据是否公开获取图像的数量
     *
     * @param isPublic 是否为公开图片 是-1 否-0
     * @return {@link Integer}
     */
    Integer countByIsPublic(@Param("isPublic") Byte isPublic);
}
