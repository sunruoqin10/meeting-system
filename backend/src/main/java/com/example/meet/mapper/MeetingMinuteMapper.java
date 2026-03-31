package com.example.meet.mapper;

import com.example.meet.entity.MeetingMinute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MeetingMinuteMapper {
    
    MeetingMinute selectById(@Param("id") String id);
    
    List<MeetingMinute> selectByCreatorId(@Param("creatorId") String creatorId);
    
    List<MeetingMinute> selectByStatus(@Param("status") String status);
    
    List<MeetingMinute> selectAll();
    
    List<MeetingMinute> search(@Param("keyword") String keyword, @Param("status") String status, 
                              @Param("creatorId") String creatorId, @Param("startDate") String startDate, 
                              @Param("endDate") String endDate);
    
    int insert(MeetingMinute meetingMinute);
    
    int update(MeetingMinute meetingMinute);
    
    int updateStatus(@Param("id") String id, @Param("status") String status);
    
    int deleteById(@Param("id") String id);
}
