package com.example.coreService.enums;

public enum StatusEnum {

    /**
     * Successfull status.
     */
    SUCCESS(1, "Success"),

    /**
     * Failed status.
     */
    FAILED(2, "Failed"),

    /**
     * In progress status.
     */
    IN_PROGRESS(3, "In-Progress");

    private final Integer id;
    private final String status;

    StatusEnum(Integer id, String profileType) {
        this.id = id;
        this.status = profileType;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
