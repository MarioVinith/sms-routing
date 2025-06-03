package com.sinch.smsrouting.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Message {

    private String id;
    private Status status;
    private String destinationNumber;
    private String content;
    private String carrier;
    private String format;

    public Message(String id, Status status) {
        this.id = id;
        this.status = status;
    }
}
