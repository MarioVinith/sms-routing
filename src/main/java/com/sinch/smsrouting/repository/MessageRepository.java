package com.sinch.smsrouting.repository;

import com.sinch.smsrouting.model.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageRepository {
    private final Map<String, Message> map = new ConcurrentHashMap<>();

    public void save(Message message) {
        map.put(message.getId(), message);
    }

    public Message get(String id) {
        return map.get(id);
    }
}
