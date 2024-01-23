package com.example.coreService.mapper;

import com.example.coreService.entity.EmailEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EmailMapper {

    @Select("SELECT * FROM email_table WHERE id IN (SELECT email_id FROM group_users_email WHERE group_users_id = #{groupId})")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "groupsUsers", column = "group_users_id", javaType = List.class,
                    many = @Many(select = "com.example.coreService.mapper.GroupUsersMapper.getGroupUsersById"))
    })
    List<EmailEntity> getEmailsByGroupId(Long groupId);
}
