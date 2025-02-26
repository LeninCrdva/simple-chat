package tec.edu.azuay.chat.service.secondary;

import nonapi.io.github.classgraph.json.JSONSerializer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import tec.edu.azuay.chat.models.dto.ChatResponse;
import tec.edu.azuay.chat.models.dto.MessageResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MessageEmitterService {

    private final Map<Long, SseEmitter> userEmitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitterForUser(Long userId){

        SseEmitter existingEmitter = userEmitters.get(userId);

        if (existingEmitter != null) {
            existingEmitter.complete();
        }

        SseEmitter emitter = new SseEmitter(0L);

        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));

        userEmitters.put(userId, emitter);

        return emitter;
    }

    public void sendToUser(Long userId, ChatResponse chatResponse){

        SseEmitter emitter = userEmitters.get(userId);

        if (emitter != null) {
            try {
                emitter.send(chatResponse, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                userEmitters.remove(userId);
                emitter.complete();
            }
        }
    }

    public void sendToUsers(List<Long> userIds, ChatResponse chatResponse) {
        userIds.forEach(userId -> sendToUser(userId, chatResponse));
    }
}
