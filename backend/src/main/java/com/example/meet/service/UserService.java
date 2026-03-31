package com.example.meet.service;

import com.example.meet.dto.LoginRequest;
import com.example.meet.dto.RegisterRequest;
import com.example.meet.entity.User;
import com.example.meet.mapper.UserMapper;
import com.example.meet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public Map<String, Object> login(LoginRequest request) {
        User user = userMapper.selectByEmail(request.getEmail());
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if (user.getStatus() == 0) {
            throw new RuntimeException("用户已被禁用");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        userMapper.updateLastLoginTime(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", getUserInfo(user));
        
        return result;
    }
    
    public void register(RegisterRequest request) {
        User existUser = userMapper.selectByEmail(request.getEmail());
        if (existUser != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        existUser = userMapper.selectByUsername(request.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }
        
        User user = new User();
        user.setId(UUID.randomUUID().toString().replace("-", ""));
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setDepartmentId(request.getDepartmentId());
        user.setRole("user");
        user.setStatus(1);
        user.setPasswordExpireDate(LocalDate.now().plusDays(90));
        
        userMapper.insert(user);
    }
    
    public User getUserInfo(String userId) {
        return userMapper.selectById(userId);
    }
    
    public void updateUserInfo(User user) {
        userMapper.update(user);
    }
    
    public void updatePassword(String userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        userMapper.updatePassword(userId, passwordEncoder.encode(newPassword));
    }
    
    private Map<String, Object> getUserInfo(User user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        userInfo.put("email", user.getEmail());
        userInfo.put("name", user.getName());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("departmentId", user.getDepartmentId());
        userInfo.put("departmentName", user.getDepartmentName());
        userInfo.put("role", user.getRole());
        userInfo.put("phone", user.getPhone());
        return userInfo;
    }
}
