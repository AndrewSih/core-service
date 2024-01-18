package com.example.coreService.entity;

import lombok.Data;

import java.util.List;

@Data
public class TemplateEntity {

    private Long id;
    private String data;
    private List<MessageEntity> messageEntities;
}
