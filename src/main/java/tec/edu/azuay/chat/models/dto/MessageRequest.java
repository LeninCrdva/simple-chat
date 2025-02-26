package tec.edu.azuay.chat.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest implements Serializable {

    private String sender;

    private Long chat;

    private String content;

    private String type;
}
