package com.example.meet.mapper;

import com.example.meet.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    
    User selectById(@Param("id") String id);
    
    User selectByEmail(@Param("email") String email);
    
    User selectByUsername(@Param("username") String username);
    
    List<User> selectAll();
    
    List<User> selectByDepartmentId(@Param("departmentId") String departmentId);
    
    int insert(User user);
    
    int update(User user);
    
    int updatePassword(@Param("id") String id, @Param("password") String password);
    
    int updateLastLoginTime(@Param("id") String id);
    
    int deleteById(@Param("id") String id);
}
