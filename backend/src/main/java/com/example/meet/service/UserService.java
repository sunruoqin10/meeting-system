package com.example.meet.service;

import com.example.meet.dto.LoginRequest;
import com.example.meet.entity.MeetingMinute;
import com.example.meet.entity.User;
import com.example.meet.mapper.*;
import com.example.meet.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Autowired
    private MeetingMinuteMapper meetingMinuteMapper;
    
    @Autowired
    private OperationLogMapper operationLogMapper;
    
    @Autowired
    private MessageMapper messageMapper;
    
    @Autowired
    private TaskAssignmentMapper taskAssignmentMapper;
    
    @Autowired
    private MinuteCommentMapper minuteCommentMapper;
    
    @Autowired
    private MinuteAttachmentMapper minuteAttachmentMapper;
    
    @Autowired
    private MeetingAttendeeMapper meetingAttendeeMapper;
    
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
        
        if (!request.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        
        userMapper.updateLastLoginTime(user.getId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", getUserInfo(user));
        
        return result;
    }
    
    public User getUserInfo(String userId) {
        return userMapper.selectById(userId);
    }
    
    public void updateUserInfo(User user) {
        if (user.getDepartmentId() != null && !user.getDepartmentId().isEmpty()) {
            if (departmentMapper.selectById(user.getDepartmentId()) == null) {
                throw new RuntimeException("指定的部门不存在");
            }
        }
        userMapper.update(user);
    }
    
    public void updatePassword(String userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        
        if (!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        userMapper.updatePassword(userId, newPassword);
    }
    
    @Transactional
    public void deleteUser(String userId) {
        User user = userMapper.selectById(userId);
        
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        if ("admin".equals(user.getRole())) {
            throw new RuntimeException("管理员账户不能删除");
        }
        
        List<MeetingMinute> createdByUser = meetingMinuteMapper.selectByCreatorId(userId);
        if (createdByUser != null && !createdByUser.isEmpty()) {
            throw new RuntimeException("该用户创建的会议纪要未删除，无法删除用户");
        }
        
        List<MeetingMinute> hostedByUser = meetingMinuteMapper.selectByHostId(userId);
        if (hostedByUser != null && !hostedByUser.isEmpty()) {
            throw new RuntimeException("该用户主持的会议纪要未删除，无法删除用户");
        }
        
        departmentMapper.updateManagerIdToNull(userId);
        
        operationLogMapper.updateUserIdToNull(userId);
        
        messageMapper.deleteByReceiverId(userId);
        
        taskAssignmentMapper.updateAssigneeIdToNull(userId);
        
        minuteCommentMapper.updateUserIdToNull(userId);
        
        minuteAttachmentMapper.updateUploaderIdToNull(userId);
        
        meetingAttendeeMapper.deleteByUserId(userId);
        
        userMapper.deleteById(userId);
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
