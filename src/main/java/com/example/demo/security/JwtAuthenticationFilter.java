package com.example.demo.security;

import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 读取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 如果没有 Authorization，或者不是 Bearer 开头，直接放行给后续过滤器
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 截取真正的 JWT 字符串
        String jwt = authHeader.substring(7);
        String username;

        try {
            // 4. 从 JWT 中解析用户名
            username = jwtUtil.getUsernameFromToken(jwt);
        } catch (Exception e) {
            // token 解析失败，直接继续后续过滤器
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 如果解析到了用户名，并且当前还没有认证信息
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 这里可以根据实际情况，从数据库查询用户信息
            // User user = userMapper.selectByUsername(username);

            // 构建认证信息
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 6. 将认证信息放入 Security 上下文
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // 7. 继续执行后续过滤器链
        filterChain.doFilter(request, response);
    }
}