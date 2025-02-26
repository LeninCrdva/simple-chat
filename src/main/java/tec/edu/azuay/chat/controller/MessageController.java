package tec.edu.azuay.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import tec.edu.azuay.chat.models.dto.MessageRequest;
import tec.edu.azuay.chat.service.interfaces.IMessageService;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final IMessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<?> getMessageByChatId(@RequestParam Long chatId) {
        return ResponseEntity.ok(messageService.getMessages(chatId));
    }

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public MessageRequest sendMessage(final MessageRequest messageResponse) {
        return messageResponse;
    }

    @PostMapping("/private")
    @ResponseStatus(HttpStatus.OK)
    public void sendToSpecificUser(@RequestBody MessageRequest messageResponse) {

        messageService.saveMessage(messageResponse);
    }
}
