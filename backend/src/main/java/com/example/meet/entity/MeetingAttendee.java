package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MeetingAttendee {
    private String id;
    private String minuteId;
    private String userId;
    private String role;
    private Integer attended;
    private LocalDateTime createdTime;
    
    private String userName;
    private String userEmail;
}
