package com.example.coreService.entity;

import lombok.Data;

import java.util.List;

@Data
public class EmailEntity {

    private Long id;
    private String email;
    private List<GroupUsersEntity> groupsUsers;
}
