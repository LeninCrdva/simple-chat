package tec.edu.azuay.chat.service.implement;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import tec.edu.azuay.chat.exceptions.ObjectNotFoundException;
import tec.edu.azuay.chat.models.dto.*;
import tec.edu.azuay.chat.models.entity.Chat;
import tec.edu.azuay.chat.models.entity.Message;
import tec.edu.azuay.chat.models.entity.User;
import tec.edu.azuay.chat.repository.IMessageRepository;
import tec.edu.azuay.chat.service.auth.JwtService;
import tec.edu.azuay.chat.service.interfaces.IChatService;
import tec.edu.azuay.chat.service.interfaces.IMessageService;
import tec.edu.azuay.chat.service.interfaces.IUserService;
import tec.edu.azuay.chat.service.secondary.MessageEmitterService;
import tec.edu.azuay.chat.util.MessageType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements IMessageService {

    private final IChatService chatService;

    private final IUserService userService;

    private final IMessageRepository messageRepository;

    private final MessageEmitterService messageEmitterService;

    private final ModelMapper modelMapper;

    private MessageResponse convertToDto(Message message) {
        MessageResponse messageResponse = modelMapper.map(message, MessageResponse.class);
        messageResponse.setChatId(message.getChat().getId());

        return messageResponse;
    }

    private LastMessageResponse convertToChatMessage(Message message) {
        return modelMapper.map(message, LastMessageResponse.class);
    }

    private Message convertToEntity(MessageRequest messageRequest) {
        Message message = modelMapper.map(messageRequest, Message.class);
        message.setSender(getUser(messageRequest.getSender()));
        message.setChat(getChat(messageRequest.getChat()));
        message.setType(MessageType.valueOf(messageRequest.getType().toUpperCase()));

        return message;
    }

    private User getUser(String username) {
        return userService.findOneByUsername(username).orElseThrow(ObjectNotFoundException::new);
    }

    private Chat getChat(Long chatId) {
        return chatService.findById(chatId);
    }

    @Override
    public List<MessageResponse> getMessages(Long chatId) {
        return messageRepository.findByChatId(chatId).stream().map(this::convertToDto).toList();
    }

    @Override
    public void saveMessage(MessageRequest messageRequest) {

        Message message = messageRepository.save(convertToEntity(messageRequest));
        List<Long> usersId = getUsersId(message.getChat().getUser());

        ChatResponse chatResponse = createChatResponse(message, messageRequest.getSender());

        messageEmitterService.sendToUsers(usersId, chatResponse);
    }

    @Override
    public void deleteMessage(Long messageId) {

    }

    private List<Long> getUsersId(List<User> users) {
        return users.stream().map(User::getId).toList();
    }

    private ChatResponse createChatResponse(Message message, String senderUsername) {
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setId(message.getChat().getId());
        chatResponse.setChatInfo(extractChatInfo(message.getChat(), senderUsername));
        chatResponse.setLastMessage(convertToChatMessage(message));

        return chatResponse;
    }

    private ChatUserResponse extractChatInfo(Chat chat, String senderUsername) {
        ChatUserResponse chatContent = new ChatUserResponse();
        User receiver = chat.getUser().stream()
                .filter(user -> !user.getUsername().equals(senderUsername))
                .findFirst()
                .orElseThrow(ObjectNotFoundException::new);


        chatContent.setReceiver(receiver.getUsername());
        chatContent.setReceiverAvatar(receiver.getImageUrl());
        chatContent.setSender(senderUsername);

        return chatContent;
    }
}
