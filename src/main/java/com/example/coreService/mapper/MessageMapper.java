package com.example.coreService.mapper;

import com.example.coreService.entity.GroupUsersEntity;
import com.example.coreService.entity.MessageEntity;
import com.example.coreService.entity.TemplateEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Select("SELECT * FROM message_table WHERE unique_message = #{uniqueMessage}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uniqueMessage", column = "unique_message"),
            @Result(property = "groupUsersId", column = "group_users_id", javaType = GroupUsersEntity.class,
                    one = @One(select = "com.example.coreService.mapper.GroupUsersMapper.getGroupUsersById")),
            @Result(property = "templateId", column = "template_id", javaType = TemplateEntity.class,
                    one = @One(select = "com.example.coreService.mapper.TemplateMapper.getTemplateById")),
            @Result(property = "fileData", column = "file_data"),
            @Result(property = "typeFile", column = "type_file"),
            @Result(property = "status", column = "status"),
            @Result(property = "messageData", column = "message_data"),
            @Result(property = "messageText", column = "message_text"),
            @Result(property = "dateStatus", column = "date_status")
    })
    MessageEntity getMessageByUniqueMessage(String uniqueMessage);

    @Insert("INSERT INTO message_table (unique_message, group_users_id, template_id, file_data, type_file, status, message_data, message_text, date_status) " +
            "VALUES (#{uniqueMessage}, #{groupUsersId.id}, #{templateId.id}, #{fileData}, #{typeFile}, #{status}, #{messageData}, #{messageText}, #{dateStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertMessage(MessageEntity messageEntity);

    @Select("SELECT * FROM message_table WHERE template_id = #{templateId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uniqueMessage", column = "unique_message"),
            @Result(property = "groupUsersId", column = "group_users_id", javaType = GroupUsersEntity.class,
                    one = @One(select = "com.example.coreService.mapper.GroupUsersMapper.getGroupUsersById")),
            @Result(property = "templateId", column = "template_id", javaType = TemplateEntity.class,
                    one = @One(select = "com.example.coreService.mapper.TemplateMapper.getTemplateById")),
            @Result(property = "fileData", column = "file_data"),
            @Result(property = "typeFile", column = "type_file"),
            @Result(property = "status", column = "status"),
            @Result(property = "messageData", column = "message_data"),
            @Result(property = "messageText", column = "message_text"),
            @Result(property = "dateStatus", column = "date_status")

    })
    List<MessageEntity> getMessagesByTemplateId(Long templateId);

    @Select("SELECT * FROM message_table WHERE group_users_id = #{groupId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uniqueMessage", column = "unique_message"),
            @Result(property = "groupUsersId", column = "group_users_id", javaType = GroupUsersEntity.class,
                    one = @One(select = "com.example.coreService.mapper.GroupUsersMapper.getGroupUsersById")),
            @Result(property = "templateId", column = "template_id", javaType = TemplateEntity.class,
                    one = @One(select = "com.example.coreService.mapper.TemplateMapper.getTemplateById")),
            @Result(property = "fileData", column = "file_data"),
            @Result(property = "typeFile", column = "type_file"),
            @Result(property = "status", column = "status"),
            @Result(property = "messageData", column = "message_data"),
            @Result(property = "messageText", column = "message_text"),
            @Result(property = "dateStatus", column = "date_status")
    })
    List<MessageEntity> getMessagesByGroupId(Long groupId);


    @Update("UPDATE message_table " +
            "SET status = #{status} " +
            "WHERE unique_message = #{uniqueMessage}")
    void updateStatus(@Param("uniqueMessage") String uniqueMessage, @Param("status") String status);
}

