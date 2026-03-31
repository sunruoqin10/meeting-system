package com.example.meet.mapper;

import com.example.meet.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {
    
    Department selectById(@Param("id") String id);
    
    List<Department> selectAll();
    
    List<Department> selectByParentId(@Param("parentId") String parentId);
    
    int insert(Department department);
    
    int update(Department department);
    
    int deleteById(@Param("id") String id);
}
