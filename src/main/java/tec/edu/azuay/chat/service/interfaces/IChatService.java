package tec.edu.azuay.chat.service.interfaces;

import tec.edu.azuay.chat.models.dto.ChatResponse;
import tec.edu.azuay.chat.models.dto.UserResponse;
import tec.edu.azuay.chat.models.entity.Chat;
import tec.edu.azuay.chat.models.entity.User;

import java.util.List;

public interface IChatService {

    Chat findById(Long id);

    void createChat(Chat chat);

    UserResponse getReceiver(Long chatId, String sender);

    List<ChatResponse> findByUserId(Long userId);

    List<User> findUsersByChatId(Long chatId);
}
