package com.example.coreService.enums;

public enum ResponseCodeEnum {
    /**
     * Invalid group users id.
     */
    INVALID_GROUP_USERS("700"),

    /**
     * Invalid template id.
     */
    INVALID_TEMPLATE_ID( "701"),

    /**
     * Message sent successfully.
     */
    MESSAGE_SENT_SUCCESSFULLY("200"),

    /**
     * Message is already exist.
     */
    MESSAGE_IS_ALREADY_EXIST("702");

    private final String code;

    ResponseCodeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
