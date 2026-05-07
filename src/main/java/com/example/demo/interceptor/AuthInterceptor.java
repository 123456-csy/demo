package com.example.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;  // 替换这里
import javax.servlet.http.HttpServletResponse; // 替换这里
import java.io.IOException;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 从请求头获取 Token
        String token = request.getHeader("Authorization");

        // Token 不存在则拦截
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            String errorJson = "{\"code\": 401, \"msg\": \"登录凭证已缺失，请重新登录\"}";
            response.getWriter().write(errorJson);
            return false;
        }

        return true;
    }
}