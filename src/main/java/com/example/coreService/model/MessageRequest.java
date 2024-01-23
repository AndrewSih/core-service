package com.example.coreService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String unique_message;
    private Long group_user;
    private Long template_id;
    private String file;
    private String type_file;
    private Map<String, String> data;
}
