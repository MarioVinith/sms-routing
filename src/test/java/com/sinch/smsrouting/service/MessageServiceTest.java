package com.sinch.smsrouting.service;

import com.sinch.smsrouting.exception.NumberValidationException;
import com.sinch.smsrouting.exception.OptedOutException;
import com.sinch.smsrouting.model.Message;
import com.sinch.smsrouting.model.MessageRequest;
import com.sinch.smsrouting.model.Status;
import com.sinch.smsrouting.repository.CarrierRegistry;
import com.sinch.smsrouting.repository.MessageRepository;
import com.sinch.smsrouting.repository.OptOutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @InjectMocks
    MessageService service;

    @Mock
    private MessageRepository messageRepo;
    @Mock
    private OptOutRepository optOutRepo;
    @Mock
    private CarrierRegistry registry;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsNumberValidScenario() {
        try {
            MessageRequest request = builMessageRequest("NUMBER_NOT_VALID");
            service.sendMessage(request);
        } catch (Exception ex) {
            assertEquals(NumberValidationException.class, ex.getClass());
        }
    }

    @Test
    void testOptedOutScenario() {
        try {
            MessageRequest request = builMessageRequest("AU");
            when(optOutRepo.isOptedOut(request.getDestinationNumber())).thenReturn(true);

            service.sendMessage(request);
        } catch (Exception ex) {
            assertEquals(OptedOutException.class, ex.getClass());
        }
    }

    @Test
    void testAUSuccessScenario() throws Exception {
        MessageRequest request = builMessageRequest("AU");
        when(optOutRepo.isOptedOut(request.getDestinationNumber())).thenReturn(false);
        when(registry.fetchCarrier(request.getDestinationNumber())).thenReturn("Telstra");
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        Message response = service.sendMessage(request);

        assertEquals(Status.SENT, response.getStatus());
        verify(messageRepo, times(1)).save(captor.capture());
        Message message = captor.getValue();
        assertEquals("Telstra", message.getCarrier());
        verify(registry, times(1)).fetchCarrier(request.getDestinationNumber());
    }

    @Test
    void testNZSuccessScenario() throws Exception {
        MessageRequest request = builMessageRequest("NZ");
        when(optOutRepo.isOptedOut(request.getDestinationNumber())).thenReturn(false);
        when(registry.fetchCarrier(request.getDestinationNumber())).thenReturn("Spark");
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        Message response = service.sendMessage(request);

        assertEquals(Status.SENT, response.getStatus());
        verify(messageRepo, times(1)).save(captor.capture());
        Message message = captor.getValue();
        assertEquals("Spark", message.getCarrier());
        verify(registry, times(1)).fetchCarrier(request.getDestinationNumber());
    }

    private MessageRequest builMessageRequest(String scenario) {
        MessageRequest request = new MessageRequest();
        request.setContent("Hello World");
        request.setFormat("SMS");
        if (scenario.equals("NUMBER_NOT_VALID")) {
            request.setDestinationNumber("+61491");
        } else if (scenario.equals("AU")) {
            request.setDestinationNumber("+61491570156");
        } else if (scenario.equals("NZ")) {
            request.setDestinationNumber("+64211234567");
        }
        return request;
    }


}
