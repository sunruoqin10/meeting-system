package com.example.meet.mapper;

import com.example.meet.entity.MinuteComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MinuteCommentMapper {
    
    MinuteComment selectById(@Param("id") String id);
    
    List<MinuteComment> selectByMinuteId(@Param("minuteId") String minuteId);
    
    List<MinuteComment> selectByUserId(@Param("userId") String userId);
    
    List<MinuteComment> selectAll();
    
    int insert(MinuteComment comment);
    
    int update(MinuteComment comment);
    
    int deleteById(@Param("id") String id);
    
    int deleteByMinuteId(@Param("minuteId") String minuteId);
    
    int updateUserIdToNull(@Param("userId") String userId);
}