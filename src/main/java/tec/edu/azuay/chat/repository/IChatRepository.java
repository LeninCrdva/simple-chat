package tec.edu.azuay.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tec.edu.azuay.chat.models.entity.Chat;
import tec.edu.azuay.chat.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface IChatRepository extends JpaRepository<Chat, Long> {

    /*
     * This method is used to find all the chats where the user is involved but not including the user
     */
    @Query(value = """
            SELECT c.* FROM Chat c
                        JOIN chat_users cu1 ON c.id = cu1.chat AND cu1.user = :userId
                        JOIN chat_users cu2 ON c.id = cu2.chat AND cu2.user != :userId""", nativeQuery = true)
    Optional<List<Chat>> findChatsByUserId(@Param("userId") Long userId);

    /*
     * This method is used to find all the chats where the user is involved
     */
    Optional<Chat> findUserIdsById(Long chatId);
}
