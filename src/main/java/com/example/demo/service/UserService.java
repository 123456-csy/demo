package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.model.entity.UserInfo; // 这里修复了
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.vo.UserDetailVO;

public interface UserService {

    // 注册
    Result<String> register(UserDTO userDTO);

    // 登录
    Result<String> login(UserDTO userDTO);

    Result<String> getUserById(Long id);
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);

    // 任务7新增
    Result<UserDetailVO> getUserDetail(Long userId);
    Result<String> updateUserInfo(UserInfo userInfo);
    Result<String> deleteUser(Long userId);
}