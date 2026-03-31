package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Template {
    private String id;
    private String name;
    private String type;
    private String content;
    private String creatorId;
    private Integer isSystem;
    private Integer status;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    private String creatorName;
}
