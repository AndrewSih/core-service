package com.example.coreService.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    private Long id;
    private String uniqueMessage;
    private GroupUsersEntity groupUsersId;
    private TemplateEntity templateId;
    private String fileData;
    private String typeFile;
    private String status;
    private String messageData;
    private String messageText;
    private LocalDateTime dateStatus;
}
