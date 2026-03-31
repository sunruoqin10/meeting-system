package com.example.meet.controller;

import com.example.meet.common.Result;
import com.example.meet.dto.LoginRequest;
import com.example.meet.dto.RegisterRequest;
import com.example.meet.entity.User;
import com.example.meet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = userService.login(request);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        try {
            userService.register(request);
            return Result.success("注册成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/user/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            return Result.success(userService.getUserInfo("u001"));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/user/info")
    public Result<Void> updateUserInfo(@RequestHeader("Authorization") String token, 
                                     @RequestBody User user) {
        try {
            userService.updateUserInfo(user);
            return Result.success("更新成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/user/password")
    public Result<Void> updatePassword(@RequestHeader("Authorization") String token,
                                     @RequestBody Map<String, String> request) {
        try {
            userService.updatePassword("u001", request.get("oldPassword"), request.get("newPassword"));
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
