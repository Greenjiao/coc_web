package com.greenjiao.coc.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@Accessors(chain = true)
@Data
@TableName("`tb_user`")
public class User {

    /**
     * 主键
     */
    @TableId(value = "`id`", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 账号
     */
    @TableField("`account`")
    private String account;

    /**
     * 密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 姓名
     */
    @TableField("`name`")
    private String name;

    /**
     * 性别
     */
    @TableField("`gender`")
    private String gender;

    /**
     * 手机号
     */
    @TableField("`telephone`")
    private String telephone;

    /**
     * 邮箱
     */
    @TableField("`email`")
    private String email;

    /**
     * 签名
     */
    @TableField("`signature`")
    private String signature;

    /**
     * 头像地址
     */
    @TableField("`avatar_address`")
    private String avatarAddress;

    /**
     * 权限
     */
    @TableField("`permission`")
    private String permission;

    /**
     * 是否被封禁
     */
    @TableField("`banned`")
    private Boolean banned;

    /**
     * 备注
     */
    @TableField("`remark`")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("`create_time`")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 删除时间
     */
    @TableField("`delete_time`")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime deleteTime;

    /**
     * 逻辑删除
     */
    @TableField("`is_deleted`")
    private Integer isDeleted;
}
