package tec.edu.azuay.chat.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {

    private Long id;

    private String username;

    private String imageUrl;
}
