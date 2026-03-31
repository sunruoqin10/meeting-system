package com.example.meet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MeetingAttendeeMapper {
    
    int insert(@Param("id") String id, @Param("minuteId") String minuteId, @Param("userId") String userId, 
              @Param("role") String role, @Param("attended") Integer attended);
    
    int deleteById(@Param("id") String id);
    
    int deleteByMinuteId(@Param("minuteId") String minuteId);
    
    int deleteByUserId(@Param("userId") String userId);
}