package tec.edu.azuay.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tec.edu.azuay.chat.models.entity.Message;

import java.util.List;

public interface IMessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatId(Long chatId);


}
