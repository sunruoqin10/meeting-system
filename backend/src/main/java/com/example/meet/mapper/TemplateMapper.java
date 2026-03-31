package com.example.meet.mapper;

import com.example.meet.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemplateMapper {
    
    Template selectById(@Param("id") String id);
    
    List<Template> selectByType(@Param("type") String type);
    
    List<Template> selectByCreatorId(@Param("creatorId") String creatorId);
    
    List<Template> selectAll();
    
    List<Template> selectSystemTemplates();
    
    int insert(Template template);
    
    int update(Template template);
    
    int deleteById(@Param("id") String id);
}
