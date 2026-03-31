package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Message {
    private String id;
    private String receiverId;
    private String type;
    private String title;
    private String content;
    private String relatedId;
    private Integer isRead;
    private LocalDateTime createdTime;
}
