package tec.edu.azuay.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import tec.edu.azuay.chat.models.dto.Message;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Message sendMessage(final Message message) throws Exception {
        System.out.println("Message sent: " + message.getText());
        return message;
    }

    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload Message message) {
        System.out.println("Message sent to specific user named:" + message.getTo() + " and the message is: "+ message.getText());
        simpMessagingTemplate.convertAndSendToUser(message.getTo(), "/specific", message);
    }
}
