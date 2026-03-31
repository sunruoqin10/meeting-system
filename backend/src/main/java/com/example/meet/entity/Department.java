package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Department {
    private String id;
    private String name;
    private String parentId;
    private String managerId;
    private Integer level;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    private String managerName;
}
