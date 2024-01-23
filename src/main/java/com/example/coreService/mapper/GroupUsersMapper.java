package com.example.coreService.mapper;

import com.example.coreService.entity.GroupUsersEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupUsersMapper {

    @Select("SELECT * FROM group_users_table WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "groupName", column = "group_name"),
            @Result(property = "emails", column = "id", javaType = List.class,
                    many = @Many(select = "com.example.coreService.mapper.EmailMapper.getEmailsByGroupId")),
            @Result(property = "messageEntities", column = "id", javaType = List.class,
                    many = @Many(select = "com.example.coreService.mapper.MessageMapper.getMessagesByGroupId"))
    })
    GroupUsersEntity getGroupUsersById(Long id);

    @Insert("INSERT INTO group_users_table (group_name) VALUES (#{groupName})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void setGroupUsers(GroupUsersEntity groupUsersEntity);
}
