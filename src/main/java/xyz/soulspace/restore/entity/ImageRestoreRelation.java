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
 * @since 2023-05-04 11:19:25
 */
@Getter
@Setter
@TableName("im_i_image_restore_relation")
public class ImageRestoreRelation extends Model<ImageRestoreRelation> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("image_id")
    private Long imageId;

    @TableField("restore_image_id")
    private Long restoreImageId;

    @TableField("restore_type")
    private String restoreType;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
