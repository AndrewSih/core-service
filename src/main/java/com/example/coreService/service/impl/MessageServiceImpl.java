package com.example.coreService.service.impl;

import com.example.coreService.entity.EmailEntity;
import com.example.coreService.entity.GroupUsersEntity;
import com.example.coreService.entity.MessageEntity;
import com.example.coreService.entity.TemplateEntity;
import com.example.coreService.enums.ResponseCodeEnum;
import com.example.coreService.enums.ResponseMessageEnum;
import com.example.coreService.enums.StatusEnum;
import com.example.coreService.mapper.*;
import com.example.coreService.model.*;
import com.example.coreService.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageMapper messageMapper;
    private final GroupUsersMapper groupUsersMapper;
    private final TemplateMapper templateMapper;

    private final EmailMapper emailMapper;
    private final HandlerMapper handlerMapper;
    private final RabbitTemplate rabbitTemplate;

    @Value("${queue.send-to-mail-service}")
    private String toMailServiceQueue;

    /**
     * Method to check and save message to DB. And send to Smpt Mail Service.
     *
     * @param messageRequest MessageRequest.class
     * @return MessageStatusResponse.class
     * @throws JsonProcessingException
     */
    @Override
    public MessageStatusResponse saveMessage(MessageRequest messageRequest) throws JsonProcessingException {
        MessageStatusResponse response = buildSuccessResponse();

        MessageEntity messageEntity = messageMapper.getMessageByUniqueMessage(messageRequest.getUnique_message());
        TemplateEntity templateEntity = templateMapper.getTemplateById(messageRequest.getTemplate_id());
        GroupUsersEntity usersEntity = groupUsersMapper.getGroupUsersById(messageRequest.getGroup_user());

        if (messageAlreadyExists(messageEntity, response)) {
            return response;
        }

        if (templateDoesNotExist(templateEntity, response)) {
            return response;
        }

        if (groupUsersDoNotExist(usersEntity, response)) {
            return response;
        }

        MessageEntity messageEntityToSave = buildMessageEntity(messageRequest, templateEntity, usersEntity);
        String messageText;
        if (messageRequest.getData() != null) {
            messageText = changeString(messageRequest.getData(),
                    templateMapper.getTemplateById(messageRequest.getTemplate_id()).getData());
            messageEntityToSave.setMessageText(messageText);
        } else {
            messageText = templateMapper.getTemplateById(messageRequest.getTemplate_id()).getData();
            messageEntityToSave.setMessageText(messageText);
        }
        messageMapper.insertMessage(messageEntityToSave);
        List<EmailEntity> emailEntities = emailMapper.getEmailsByGroupId(usersEntity.getId());

        SendToEmailServRequest sendToEmailServRequest = SendToEmailServRequest.builder()
                .emails(emailEntities.stream().map(EmailEntity::getEmail).collect(Collectors.toList()))
                .uniqueMessage(messageRequest.getUnique_message())
                .message(messageText).build();
        rabbitTemplate.convertAndSend(toMailServiceQueue, sendToEmailServRequest);
        return response;
    }

    /**
     * Method to get unique message form DB.
     *
     * @param uniqueMessage string
     * @return MessageUnique.class
     * @throws JsonProcessingException
     */
    @Override
    public MessageUnique getUniqueMessage(String uniqueMessage) throws JsonProcessingException {
        MessageUnique messageUnique = new MessageUnique();
        MessageEntity messageEntity = messageMapper.getMessageByUniqueMessage(uniqueMessage);
        if (messageEntity == null) {
            messageUnique.setStatus(StatusEnum.FAILED.getStatus());
            messageUnique.setDate(LocalDateTime.now().toString());
            messageUnique.setError(ResponseMessageEnum.MESSAGE_WITH_SUCH_UNIQUE_CODE_NOT_FOUND.getMessage());
            return messageUnique;
        }
        messageUnique = handlerMapper.mapToDto(messageEntity);
        if (messageEntity.getStatus().equals(StatusEnum.FAILED.getStatus())) {
            messageUnique.setError((ResponseMessageEnum.MESSAGE_NOT_SENT_TO_EMAIL.getMessage()));
        }
        messageUnique.setDate(LocalDateTime.now().toString());
        return messageUnique;
    }

    /**
     * Method to change message status.
     *
     * @param response ResponseFromMailServ.class
     */
    @Override
    public void changeMessageStatus(ResponseFromMailServ response) {
        messageMapper.updateStatus(response.getUniqueMessage(), response.getStatus());
    }

    /**
     * Method to build MessageStatusResponse.class with success status.
     *
     * @return MessageStatusResponse.class
     */
    private MessageStatusResponse buildSuccessResponse() {
        return MessageStatusResponse.builder()
                .status(StatusEnum.SUCCESS.getStatus())
                .responseCode(ResponseCodeEnum.MESSAGE_SENT_SUCCESSFULLY.getCode())
                .message(ResponseMessageEnum.MESSAGE_SENT_SUCCESSFULLY.getMessage())
                .build();
    }

    /**
     * Method to check TemplateEntity.class for null.
     *
     * @param templateEntity TemplateEntity.class
     * @param response       MessageStatusResponse.class
     */
    private boolean templateDoesNotExist(TemplateEntity templateEntity, MessageStatusResponse response) {
        if (templateEntity == null) {
            setFailedResponse(response, ResponseMessageEnum.INVALID_TEMPLATE_ID,
                    ResponseCodeEnum.MESSAGE_IS_ALREADY_EXIST);
            return true;
        }

        return false;
    }

    /**
     * Method to check GroupUsersEntity.class for null.
     *
     * @param usersEntity GroupUsersEntity.class
     * @param response    MessageStatusResponse.class
     */
    private boolean groupUsersDoNotExist(GroupUsersEntity usersEntity, MessageStatusResponse response) {
        if (usersEntity == null) {
            setFailedResponse(response, ResponseMessageEnum.INVALID_GROUP_USERS, ResponseCodeEnum.INVALID_GROUP_USERS);
            return true;
        }

        return false;
    }

    /**
     * Method to check MessageEntity.class for null.
     *
     * @param messageEntity MessageEntity.class
     * @param response      MessageStatusResponse.class
     */
    private boolean messageAlreadyExists(MessageEntity messageEntity, MessageStatusResponse response) {
        if (messageEntity != null) {
            setFailedResponse(response, ResponseMessageEnum.MESSAGE_IS_ALREADY_EXIST,
                    ResponseCodeEnum.MESSAGE_IS_ALREADY_EXIST);
            return true;
        }

        return false;
    }

    /**
     * Method to set failed response.
     *
     * @param response MessageStatusResponse.class
     * @param message ResponseMessageEnum.class
     * @param code ResponseCodeEnum.class
     */
    private void setFailedResponse(MessageStatusResponse response, ResponseMessageEnum message, ResponseCodeEnum code) {
        response.setStatus(StatusEnum.FAILED.getStatus());
        response.setMessage(message.getMessage());
        response.setResponseCode(code.getCode());
    }

    /**
     * Method to build MessageEntity.class.
     *
     * @param messageRequest MessageRequest.class
     * @param templateEntity TemplateEntity.class
     * @param usersEntity    GroupUsersEntity.class
     * @return MessageEntity.class
     * @throws JsonProcessingException
     */
    private MessageEntity buildMessageEntity
    (MessageRequest messageRequest, TemplateEntity templateEntity, GroupUsersEntity usersEntity)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return MessageEntity.builder()
                .uniqueMessage(messageRequest.getUnique_message())
                .groupUsersId(usersEntity)
                .templateId(templateEntity)
                .fileData(messageRequest.getFile())
                .typeFile(messageRequest.getType_file())
                .status(StatusEnum.IN_PROGRESS.getStatus())
                .messageData(messageRequest.getData() != null
                        ? objectMapper.writeValueAsString(messageRequest.getData()) : null)
                .messageText("IN PROGRESS")
                .dateStatus(LocalDateTime.now())
                .build();
    }

    /**
     * Method for replacing keywords in a message.
     *
     * @param map      Map<String, String>
     * @param template string
     * @return string
     */
    private String changeString(Map<String, String> map, String template) {
        final String[] keys = map.keySet().toArray(new String[0]);
        final String[] values = map.values().toArray(new String[0]);
        return StringUtils.replaceEach(template, keys, values);
    }
}
