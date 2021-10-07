package com.fei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fei.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fei
 * @since 2021-09-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
