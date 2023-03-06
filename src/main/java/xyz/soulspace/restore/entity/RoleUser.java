package xyz.soulspace.restore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2023-02-20 03:46:51
 */
@Getter
@Setter
@TableName("um_t_role_user")
public class RoleUser extends Model<RoleUser> {

    @TableField("id")
    private Long id;

    @TableField("role_id")
    private Integer roleId;

    @TableField("user_id")
    private Integer userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return null;
    }
}
