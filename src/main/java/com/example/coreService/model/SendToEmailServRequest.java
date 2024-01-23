package com.example.coreService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendToEmailServRequest implements Serializable {

    private String message;
    private List<String> emails;
    private String uniqueMessage;
}
