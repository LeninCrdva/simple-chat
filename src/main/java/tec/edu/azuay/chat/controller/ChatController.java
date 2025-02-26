package tec.edu.azuay.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tec.edu.azuay.chat.service.interfaces.IChatService;
import tec.edu.azuay.chat.service.secondary.MessageEmitterService;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final IChatService chatService;

    private final MessageEmitterService userEmitterService;

    @GetMapping("/chat-by-user")
    public List<?> getChatMessages(@RequestParam Long userId) {
        return chatService.findByUserId(userId);
    }

    @GetMapping(value = {"/{userId}/events"}, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getChatEvents(@PathVariable Long userId) {

        return userEmitterService.createEmitterForUser(userId);
    }
}
