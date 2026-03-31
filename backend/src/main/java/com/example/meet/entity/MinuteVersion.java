package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MinuteVersion {
    private String id;
    private String minuteId;
    private String version;
    private String content;
    private String changeLog;
    private String creatorId;
    private LocalDateTime createdTime;
    
    private String creatorName;
}
