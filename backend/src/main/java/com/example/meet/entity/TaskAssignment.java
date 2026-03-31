package com.example.meet.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskAssignment {
    private String id;
    private String minuteId;
    private String assigneeId;
    private String taskContent;
    private LocalDate deadline;
    private String status;
    private String priority;
    private LocalDateTime completedTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    private String assigneeName;
    private String minuteTitle;
}
