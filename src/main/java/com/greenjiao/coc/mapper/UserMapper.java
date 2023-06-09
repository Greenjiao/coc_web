package com.greenjiao.coc.mapper;

import com.greenjiao.coc.bean.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yan
 * @since 2023-06-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
