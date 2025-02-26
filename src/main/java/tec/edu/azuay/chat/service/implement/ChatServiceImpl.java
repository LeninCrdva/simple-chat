package tec.edu.azuay.chat.service.implement;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tec.edu.azuay.chat.exceptions.ObjectNotFoundException;
import tec.edu.azuay.chat.models.dto.*;
import tec.edu.azuay.chat.models.entity.Chat;
import tec.edu.azuay.chat.models.entity.User;
import tec.edu.azuay.chat.repository.IChatRepository;
import tec.edu.azuay.chat.service.interfaces.IChatService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {

    private final IChatRepository chatRepository;

    private final ModelMapper modelMapper;

    private UserResponse convertToDto(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    private ChatResponse convertToDto(Chat chat, Long senderId) {
        ChatResponse chatResponse = new ChatResponse();

        ChatUserResponse chatContent = extractChatContent(chat.getUser(), senderId);

        LastMessageResponse lastMessage = chat.getMessage().stream()
                .map(message -> modelMapper.map(message, LastMessageResponse.class))
                .reduce((first, second) -> second)
                .orElse(new LastMessageResponse());

        chatResponse.setId(chat.getId());
        chatResponse.setChatInfo(chatContent);
        chatResponse.setLastMessage(lastMessage);

        return chatResponse;
    }

    @Override
    public Chat findById(Long id) {
        return chatRepository.findById(id).orElseThrow(ObjectNotFoundException::new);
    }

    @Override
    public void createChat(Chat chat) {
        chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse getReceiver(Long chatId, String sender) {
        List<User> users = chatRepository.findById(chatId).orElseThrow(ObjectNotFoundException::new).getUser();

        User user = users.stream()
                .filter(u -> !u.getUsername().equals(sender))
                .findFirst()
                .orElseThrow(ObjectNotFoundException::new);

        return convertToDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatResponse> findByUserId(Long userId) {
        return chatRepository.findChatsByUserId(userId).orElseThrow(ObjectNotFoundException::new).stream().map(u -> convertToDto(u, userId)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findUsersByChatId(Long chatId) {

        Chat chat = chatRepository.findUserIdsById(chatId).orElseThrow(ObjectNotFoundException::new);

        return chat.getUser();
    }

    private ChatUserResponse extractChatContent(List<User> user, Long senderId) {
        ChatUserResponse chatUserResponse = new ChatUserResponse();
        UserResponse userResponse = convertToReceive(user.stream().filter(u -> !u.getId().equals(senderId)).findFirst().orElseThrow(ObjectNotFoundException::new));
        chatUserResponse.setSender(SecurityContextHolder.getContext().getAuthentication().getName());
        chatUserResponse.setReceiver(userResponse.getUsername());
        chatUserResponse.setReceiverAvatar(userResponse.getImageUrl());
        return chatUserResponse;
    }

    private UserResponse convertToReceive(User user) {
        return UserResponse.builder().username(user.getUsername()).imageUrl(user.getImageUrl()).build();
    }
}
