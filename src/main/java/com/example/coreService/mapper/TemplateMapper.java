package com.example.coreService.mapper;

import com.example.coreService.entity.TemplateEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TemplateMapper {

    @Select("SELECT * FROM template_table WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "data", column = "data"),
            @Result(property = "messageEntities", column = "id", javaType = List.class,
                    many = @Many(select = "com.example.coreService.mapper.MessageMapper.getMessagesByTemplateId"))
    })
    TemplateEntity getTemplateById(Long id);


    @Insert("INSERT INTO template_table (data) VALUES (#{data})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void setTemplate(TemplateEntity templateEntity);
}
