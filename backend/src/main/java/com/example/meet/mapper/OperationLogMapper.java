package com.example.meet.mapper;

import com.example.meet.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OperationLogMapper {
    
    OperationLog selectById(@Param("id") String id);
    
    List<OperationLog> selectByUserId(@Param("userId") String userId);
    
    List<OperationLog> selectAll();
    
    int insert(OperationLog operationLog);
    
    int update(OperationLog operationLog);
    
    int deleteById(@Param("id") String id);
    
    int updateUserIdToNull(@Param("userId") String userId);
}