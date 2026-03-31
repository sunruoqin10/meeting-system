package com.example.meet.mapper;

import com.example.meet.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MessageMapper {
    
    Message selectById(@Param("id") String id);
    
    List<Message> selectByReceiverId(@Param("receiverId") String receiverId);
    
    List<Message> selectUnreadByReceiverId(@Param("receiverId") String receiverId);
    
    List<Message> selectAll();
    
    int insert(Message message);
    
    int update(Message message);
    
    int deleteById(@Param("id") String id);
    
    int markAsRead(@Param("id") String id);
    
    int markAllAsRead(@Param("receiverId") String receiverId);
    
    int deleteByReceiverId(@Param("receiverId") String receiverId);
}