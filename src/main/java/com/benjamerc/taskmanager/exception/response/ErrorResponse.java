package com.benjamerc.taskmanager.exception.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private int status;
    private String message;

    @Builder.Default
    private long timestamp = System.currentTimeMillis();

}
