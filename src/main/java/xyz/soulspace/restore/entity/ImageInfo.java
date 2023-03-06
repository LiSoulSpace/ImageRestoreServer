package xyz.soulspace.restore.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONAutowired;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author soulspace
 * @since 2023-02-20 07:04:23
 */
@Getter
@Setter
@TableName("im_i_image_info")
public class ImageInfo extends Model<ImageInfo> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("image_name")
    private String imageName;

    @TableField("image_path")
    private String imagePath;

    @TableField("image_md5")
    private String imageMd5;

    @TableField("image_type")
    private String imageType;

    @TableField("image_height")
    private Integer imageHeight;

    @TableField("image_width")
    private Integer imageWidth;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
