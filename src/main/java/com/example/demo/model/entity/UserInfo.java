package com.example.demo.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_info")
public class UserInfo {
    private Long id;
    private Long userId;
    private String realName;
    private String phone;
    private String address;
}