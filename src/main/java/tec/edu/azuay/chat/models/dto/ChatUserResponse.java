package tec.edu.azuay.chat.models.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatUserResponse implements Serializable {

    private String sender;

    private String receiver;

    private String receiverAvatar;
}
