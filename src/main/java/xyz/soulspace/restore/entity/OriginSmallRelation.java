package xyz.soulspace.restore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author soulspace
 * @since 2023-04-26 10:31:06
 */
@Getter
@Setter
@TableName("im_i_origin_small_relation")
public class OriginSmallRelation extends Model<OriginSmallRelation> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("origin_img_id")
    private Long originImgId;

    @TableField("small_img_id")
    private Long smallImgId;

    @TableField("is_public")
    private Byte isPublic;

    @TableField("description")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
