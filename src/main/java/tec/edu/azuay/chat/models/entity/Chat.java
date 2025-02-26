package tec.edu.azuay.chat.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "message"})
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinTable(name = "chat_users",
            joinColumns = @JoinColumn(name = "chat"),
            inverseJoinColumns = @JoinColumn(name = "user"))
    private List<User> user;

    @OneToMany(targetEntity = Message.class, fetch = FetchType.LAZY, mappedBy = "chat")
    private List<Message> message;

    @PrePersist
    public void prePersist() {
        this.name = UUID.randomUUID().toString();
    }
}
