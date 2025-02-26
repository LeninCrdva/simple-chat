package tec.edu.azuay.chat.models.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatResponse implements Serializable {

    private Long id;

    private ChatUserResponse chatInfo;

    private LastMessageResponse lastMessage;
}
