package xyz.soulspace.restore.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author soulspace
 * @since 2023-02-28 10:26:01
 */
@Getter
@Setter
@TableName("test")
public class Test extends Model<Test> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("test_column")
    private String testColumn;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
