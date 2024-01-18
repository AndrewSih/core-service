package com.example.coreService.listener;

import com.example.coreService.model.MessageRequest;
import com.example.coreService.model.MessageStatusResponse;
import com.example.coreService.model.MessageUnique;
import com.example.coreService.model.ResponseFromMailServ;
import com.example.coreService.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableRabbit
public class MessageListener {
    private final MessageService messageService;
    private final RabbitTemplate rabbitTemplate;

    /**
     * Method to get message from answering service, save to DB, send to SmtpMailService. Return response to answering service.
     *
     * @param data    MessageRequest.class
     * @param message message with properties
     * @throws JsonProcessingException
     */
    @RabbitListener(queues = "${queue.save-and-send}")
    public void saveAndSendMessage(MessageRequest data, Message message) throws JsonProcessingException {
        MessageStatusResponse response = messageService.saveMessage(data);
        String replyTo = message.getMessageProperties().getReplyTo();
        String json = getJson(response);
        rabbitTemplate.convertAndSend(replyTo, json);
    }

    /**
     * Method to get from DB and send unique message.
     *
     * @param uniqueMessage string
     * @param message       message with properties
     * @throws JsonProcessingException
     */
    @RabbitListener(queues = "${queue.get-unique-message}")
    public void sendUniqueMessage(String uniqueMessage, Message message) throws JsonProcessingException {

        MessageUnique messageUnique = messageService.getUniqueMessage(uniqueMessage);
        String replyTo = message.getMessageProperties().getReplyTo();
        String json = getJson(messageUnique);
        rabbitTemplate.convertAndSend(replyTo, json);
    }

    /**
     * Method to get response from smpt mail service and change message status
     *
     * @param response ResponseFromMailServ.class
     */
    @RabbitListener(queues = "${queue.send-from-mail-service}")
    public void getResponseFromMailService(ResponseFromMailServ response) {
        messageService.changeMessageStatus(response);
    }

    /**
     * Method to map object to string.
     *
     * @param messageUnique object
     * @return string
     * @throws JsonProcessingException
     */
    private String getJson(Object messageUnique) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(messageUnique);

    }
}
