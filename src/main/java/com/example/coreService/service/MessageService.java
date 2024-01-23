package com.example.coreService.service;

import com.example.coreService.model.MessageRequest;
import com.example.coreService.model.MessageStatusResponse;
import com.example.coreService.model.MessageUnique;
import com.example.coreService.model.ResponseFromMailServ;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface MessageService {

    MessageStatusResponse saveMessage(MessageRequest messageRequest) throws JsonProcessingException;

    MessageUnique getUniqueMessage(String uniqueMessage) throws JsonProcessingException;

    void changeMessageStatus(ResponseFromMailServ response);
}
