package com.example.interview.controller;

import com.example.interview.entity.User;
import com.example.interview.mapper.UserMapper;
import com.example.interview.util.Result;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public Result<String> register(@RequestParam String username, @RequestParam String password) {
        User existUser = userMapper.getUserByUsername(username);
        if (existUser != null) {
            return Result.fail("用户名已存在");
        }
        User newUser = new User();
        // 生成唯一的业务ID返回给前端
        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8);
        newUser.setUserId(userId);
        newUser.setUsername(username);
        newUser.setPassword(password); // 生产环境请用 BCrypt 加密
        
        userMapper.insertUser(newUser);
        return Result.success(userId);
    }

    @PostMapping("/login")
    public Result<String> login(@RequestParam String username, @RequestParam String password) {
        User user = userMapper.getUserByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return Result.fail("账号或密码错误");
        }
        // 登录成功，返回 userId 供前端缓存
        return Result.success(user.getUserId());
    }
}

