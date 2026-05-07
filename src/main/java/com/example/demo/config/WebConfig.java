package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 这里什么都不用写！
    // 所有拦截、权限、登录 全部交给 Spring Security 管理

}