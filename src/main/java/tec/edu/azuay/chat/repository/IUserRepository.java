package tec.edu.azuay.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tec.edu.azuay.chat.models.entity.User;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsername(String username);
}
