package com.sinch.smsrouting.service;

import com.sinch.smsrouting.exception.NumberValidationException;
import com.sinch.smsrouting.exception.OptedOutException;
import com.sinch.smsrouting.model.Message;
import com.sinch.smsrouting.model.MessageRequest;
import com.sinch.smsrouting.model.Status;
import com.sinch.smsrouting.repository.CarrierRegistry;
import com.sinch.smsrouting.repository.MessageRepository;
import com.sinch.smsrouting.repository.OptOutRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.sinch.smsrouting.util.SmsRoutingUtil.*;

@Slf4j
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private OptOutRepository optOutRepo;
    @Autowired
    private CarrierRegistry registry;

    @Autowired
    private ScheduledExecutorService scheduledExecutor;

    public Message sendMessage(MessageRequest request) throws Exception {
        Message message = new Message();

        processInvalidNumber(request.getDestinationNumber());
        processOptedOutScenario(request.getDestinationNumber());

        long s = System.currentTimeMillis() / 1000;

        message.setId(UUID.randomUUID().toString());
        message.setDestinationNumber(request.getDestinationNumber());
        message.setContent(request.getContent());
        message.setFormat(request.getFormat());

        message.setCarrier(registry.fetchCarrier(request.getDestinationNumber()));

        if (request.getTimeStamp() != null) {
            message.setStatus(Status.PENDING);

            scheduledExecutor.schedule(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {}

                log.info("Message delivered to the Customer - {}", request.getDestinationNumber());
                message.setStatus(Status.DELIVERED);
                messageRepo.save(message);
            }, s - request.getTimeStamp(), TimeUnit.SECONDS);
        } else {
            message.setStatus(Status.SENT);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {}

                log.info("Message delivered to the Customer - {}", request.getDestinationNumber());
                message.setStatus(Status.DELIVERED);
                messageRepo.save(message);
            }).start();
        }

        log.info("Message triggered to the Carrier - {} for {}", message.getCarrier(), request.getDestinationNumber());
        messageRepo.save(message);

        return new Message(message.getId(), message.getStatus());
    }

    public Message getMessage(String id) {
        return messageRepo.get(id);
    }

    public void optOut(String number) {
        optOutRepo.optOut(number);
    }

    private void processOptedOutScenario(String number) throws OptedOutException {
        if (optOutRepo.isOptedOut(number)) {
            log.error("Provided Number - {} has opted out for sending messages.", number);
            throw new OptedOutException("NUMBER_OPTED_OUT");
        }
    }

    private void processInvalidNumber(String number) throws NumberValidationException {
        if (!isValidNumber(number)) {
            log.error("Provided Number - {} is not Valid.", number);
            throw new NumberValidationException("NUMBER_NOT_VALID");
        }
    }
}
