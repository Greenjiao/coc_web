package com.greenjiao.coc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@Data
@TableName("`dict_permission`")
public class Permission {

    /**
     * 主键
     */
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 权重
     */
    @TableField("`weight`")
    private Integer weight;

    /**
     * 权限名
     */
    @TableField("`name`")
    private String name;
}
