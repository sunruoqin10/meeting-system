package com.example.meet.mapper;

import com.example.meet.entity.TaskAssignment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TaskAssignmentMapper {
    
    TaskAssignment selectById(@Param("id") String id);
    
    List<TaskAssignment> selectByMinuteId(@Param("minuteId") String minuteId);
    
    List<TaskAssignment> selectByAssigneeId(@Param("assigneeId") String assigneeId);
    
    List<TaskAssignment> selectAll();
    
    int insert(TaskAssignment taskAssignment);
    
    int update(TaskAssignment taskAssignment);
    
    int deleteById(@Param("id") String id);
    
    int deleteByMinuteId(@Param("minuteId") String minuteId);
    
    int updateAssigneeIdToNull(@Param("assigneeId") String assigneeId);
}