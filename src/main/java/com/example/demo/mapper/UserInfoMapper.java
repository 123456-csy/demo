package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.model.entity.UserInfo;
import com.example.demo.model.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    // 联查用户详情（sys_user + user_info）
    @Select("SELECT " +
            "u.id AS userId, " +
            "u.username, " +
            "ui.real_name AS realName, " +
            "ui.phone, " +
            "ui.address " +
            "FROM sys_user u " +
            "LEFT JOIN user_info ui ON u.id = ui.user_id " +
            "WHERE u.id = #{userId}")
    UserDetailVO getUserDetail(@Param("userId") Long userId);
}