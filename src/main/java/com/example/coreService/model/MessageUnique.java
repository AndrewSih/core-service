package com.example.coreService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageUnique {
    private String unique_message;
    private Long group_user;
    private Long template_id;
    private String file;
    private String type_file;
    private Map<String, String> data;
    private String error; //
    private String message_text;
    private String date;//
    private String status;
    private String date_status;
}
