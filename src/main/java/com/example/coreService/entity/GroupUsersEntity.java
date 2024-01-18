package com.example.coreService.entity;

import lombok.Data;

import java.util.List;

@Data
public class GroupUsersEntity {

    private Long id;
    private String groupName;
    private List<EmailEntity> emails;
    private List<MessageEntity> messageEntities;
}
