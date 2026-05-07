package com.example.demo.model.dto;

public class UserDTO {
    // 接收前端传递的用户名
    private String username;
    // 接收前端传递的密码
    private String password;

    // Getter 方法
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setter 方法
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}