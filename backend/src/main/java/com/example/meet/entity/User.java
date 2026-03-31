package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String avatar;
    private String departmentId;
    private String role;
    private String phone;
    private Integer status;
    private LocalDateTime lastLoginTime;
    private LocalDate passwordExpireDate;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    private String departmentName;
}
