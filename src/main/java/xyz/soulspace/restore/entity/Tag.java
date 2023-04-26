package xyz.soulspace.restore.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author soulspace
 * @since 2023-03-27 04:00:28
 */
@Getter
@Setter
@TableName("im_i_tag")
public class Tag extends Model<Tag> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("tag_name")
    private String tagName;

    @TableField("tag_name_alias")
    private String tagNameAlias;

    @TableField("is_public_tag")
    private int isPublicTag;

    @TableField("is_main_tag")
    private int isMainTag;

    @TableField("tag_creator_id")
    private Long tagCreatorId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
