package com.example.meet.mapper;

import com.example.meet.entity.MinuteAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MinuteAttachmentMapper {
    
    MinuteAttachment selectById(@Param("id") String id);
    
    List<MinuteAttachment> selectByMinuteId(@Param("minuteId") String minuteId);
    
    List<MinuteAttachment> selectByVersionId(@Param("versionId") String versionId);
    
    List<MinuteAttachment> selectByUploaderId(@Param("uploaderId") String uploaderId);
    
    List<MinuteAttachment> selectAll();
    
    int insert(MinuteAttachment attachment);
    
    int update(MinuteAttachment attachment);
    
    int deleteById(@Param("id") String id);
    
    int deleteByMinuteId(@Param("minuteId") String minuteId);
    
    int updateUploaderIdToNull(@Param("uploaderId") String uploaderId);
}