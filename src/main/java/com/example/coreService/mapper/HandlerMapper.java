package com.example.coreService.mapper;

import com.example.coreService.entity.MessageEntity;
import com.example.coreService.model.MessageUnique;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HandlerMapper {
    /**
     * Method to map MessageEntity.class to MessageUnique.class.
     *
     * @param entity MessageEntity.class
     * @return MessageUnique.class
     * @throws JsonProcessingException
     */
    public MessageUnique mapToDto(MessageEntity entity) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return MessageUnique.builder()
                .unique_message(entity.getUniqueMessage())
                .group_user(entity.getGroupUsersId().getId())
                .template_id(entity.getTemplateId().getId())
                .file(entity.getFileData())
                .type_file(entity.getTypeFile())
                .data(entity.getMessageData() != null ? objectMapper.readValue(entity.getMessageData(), Map.class) : null)
                .message_text(entity.getMessageText())
                .status(entity.getStatus())
                .date_status(entity.getDateStatus().toString())
                .build();
    }
}
