package com.example.demo.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import com.example.demo.entity.User;
import com.example.demo.model.entity.UserInfo;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.model.vo.UserDetailVO;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.UserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String CACHE_KEY = "user:detail:";

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 新增：注入 JwtUtil
    @javax.annotation.Resource
    private JwtUtil jwtUtil;

    @Override
    public Result<String> register(UserDTO userDTO) {
        User exist = getOne(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        if (exist != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        save(user);

        return Result.success("注册成功");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        User user = getOne(new QueryWrapper<User>().eq("username", userDTO.getUsername()));
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        if (!user.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        // 登录成功后，调用 JwtUtil 生成 Token
        String jwt = jwtUtil.generateToken(userDTO.getUsername());
        return Result.success(jwt);
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = getById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success("查询成功：" + user.getUsername());
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        Page<User> result = baseMapper.selectPage(page, null);
        return Result.success(result);
    }

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY + userId;

        String json = stringRedisTemplate.opsForValue().get(key);
        if (json != null && !json.trim().isEmpty()) {
            try {
                UserDetailVO vo = JSONUtil.toBean(json, UserDetailVO.class);
                return Result.success(vo);
            } catch (Exception e) {
                stringRedisTemplate.delete(key);
            }
        }

        UserDetailVO userDetail = userInfoMapper.getUserDetail(userId);
        if (userDetail == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }

        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(userDetail), 10, TimeUnit.MINUTES);
        return Result.success(userDetail);
    }

    @Override
    @Transactional
    public Result<String> updateUserInfo(UserInfo userInfo) {
        userInfoMapper.updateById(userInfo);
        stringRedisTemplate.delete(CACHE_KEY + userInfo.getUserId());
        return Result.success("更新成功");
    }

    @Override
    @Transactional
    public Result<String> deleteUser(Long userId) {
        removeById(userId);
        userInfoMapper.delete(new QueryWrapper<UserInfo>().eq("user_id", userId));
        stringRedisTemplate.delete(CACHE_KEY + userId);
        return Result.success("删除成功");
    }
}