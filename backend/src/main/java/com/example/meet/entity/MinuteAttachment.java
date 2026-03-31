package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MinuteAttachment {
    private String id;
    private String minuteId;
    private String versionId;
    private String fileName;
    private Long fileSize;
    private String fileType;
    private String filePath;
    private String uploaderId;
    private LocalDateTime createdTime;
    
    private String uploaderName;
}
