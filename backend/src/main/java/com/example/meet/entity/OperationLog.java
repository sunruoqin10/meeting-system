package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OperationLog {
    private String id;
    private String userId;
    private String userName;
    private String module;
    private String operation;
    private String resourceId;
    private String resourceType;
    private String details;
    private String ipAddress;
    private LocalDateTime createdTime;
}
