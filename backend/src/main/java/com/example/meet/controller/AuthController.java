package com.example.meet.controller;

import com.example.meet.common.Result;
import com.example.meet.dto.LoginRequest;
import com.example.meet.entity.User;
import com.example.meet.service.UserService;
import com.example.meet.util.JwtUtil;
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
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> result = userService.login(request);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @GetMapping("/user/info")
    public Result<User> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            String userId = getUserIdFromToken(token);
            return Result.success(userService.getUserInfo(userId));
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    @PutMapping("/user/info")
    public Result<Void> updateUserInfo(@RequestHeader("Authorization") String token, 
                                     @RequestBody User user) {
        try {
            String userId = getUserIdFromToken(token);
            user.setId(userId);
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
            String userId = getUserIdFromToken(token);
            userService.updatePassword(userId, request.get("oldPassword"), request.get("newPassword"));
            return Result.success("密码修改成功", null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    
    private String getUserIdFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return jwtUtil.getUserIdFromToken(token);
    }
}
