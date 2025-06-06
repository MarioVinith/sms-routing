package com.sinch.smsrouting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MessageRequest {

    @JsonProperty(value = "destination_number")
    @NotBlank(message = "Destination Number is required")
    private String destinationNumber;

    @NotBlank(message = "Content is required")
    private String content;

    @NotBlank(message = "Format is required")
    private String format;

    private Long timeStamp;

}
