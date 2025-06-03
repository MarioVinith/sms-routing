package com.sinch.smsrouting.web.rest;

import com.sinch.smsrouting.model.Message;
import com.sinch.smsrouting.model.MessageRequest;
import com.sinch.smsrouting.service.MessageService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class MessageController {

    @Autowired
    private MessageService service;

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody @Valid MessageRequest request) throws Exception {
        Message msg =  service.sendMessage(request);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> getMessageStatus(@PathVariable String id) {
        Message message = service.getMessage(id);
        return message != null ? ResponseEntity.ok(message) : ResponseEntity.notFound().build();
    }

    @PostMapping("/optout/{phoneNumber}")
    public ResponseEntity<Void> optOut(@PathVariable String phoneNumber) {
        service.optOut(phoneNumber);
        return ResponseEntity.accepted().build();
    }

}
