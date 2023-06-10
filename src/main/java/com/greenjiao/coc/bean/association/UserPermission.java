package com.greenjiao.coc.bean.association;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yan
 * @since 2023-06-05
 */
@Data
@TableName("`relation_user_permission`")
@AllArgsConstructor
public class UserPermission {

    /**
     * 用户主键
     */
    @MppMultiId
    @TableField("`user_id`")
    private String userId;

    /**
     * 权限主键
     */
    @MppMultiId
    @TableField("`permission_id`")
    private String permissionId;
}
