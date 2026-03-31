package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MinuteComment {
    private String id;
    private String minuteId;
    private String userId;
    private String content;
    private String parentId;
    private Integer level;
    private String mentionedUsers;
    private LocalDateTime createdTime;
    
    private String userName;
    private String userAvatar;
}
