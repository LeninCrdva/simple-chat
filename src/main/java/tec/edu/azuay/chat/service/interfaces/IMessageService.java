package tec.edu.azuay.chat.service.interfaces;

import tec.edu.azuay.chat.models.dto.MessageRequest;
import tec.edu.azuay.chat.models.dto.MessageResponse;

import java.util.List;

public interface IMessageService {

    List<MessageResponse> getMessages(Long chatId);

    void saveMessage(MessageRequest messageRequest);

    void deleteMessage(Long messageId);

}
