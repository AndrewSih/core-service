package com.example.coreService.enums;

public enum ResponseMessageEnum {

    /**
     * Invalid group users id.
     */
    INVALID_GROUP_USERS("Invalid group users id"),

    /**
     * Invalid template id.
     */
    INVALID_TEMPLATE_ID("Invalid template id"),

    /**
     * Message sent successfully.
     */
    MESSAGE_SENT_SUCCESSFULLY("Message sent successfully"),

    /**
     * Message is already exist.
     */
    MESSAGE_IS_ALREADY_EXIST("Unique number of message is already exist"),

    /**
     * Message not found.
     */
    MESSAGE_WITH_SUCH_UNIQUE_CODE_NOT_FOUND("Message with such unique code not found"),

    /**
     * Message not sent to emails.
     */
    MESSAGE_NOT_SENT_TO_EMAIL("Message saved, but didn't send to emails by technical problems");

    private final String message;

    ResponseMessageEnum(String profileType) {
        this.message = profileType;
    }

    public String getMessage() {
        return message;
    }
}
