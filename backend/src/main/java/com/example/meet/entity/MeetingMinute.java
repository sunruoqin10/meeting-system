package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MeetingMinute {
    private String id;
    private String title;
    private LocalDate meetingDate;
    private String meetingTime;
    private String location;
    private String hostId;
    private String status;
    private String currentVersion;
    private String summary;
    private String content;
    private String creatorId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    private String creatorName;
    private String hostName;
    private Integer commentCount;
    private Integer attachmentCount;
}
