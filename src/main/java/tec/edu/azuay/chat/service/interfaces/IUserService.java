package tec.edu.azuay.chat.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import tec.edu.azuay.chat.models.dto.UserRequest;
import tec.edu.azuay.chat.models.dto.UserResponse;
import tec.edu.azuay.chat.models.entity.User;

import java.util.Optional;

public interface IUserService {

    User createOneUser(UserRequest newUser, MultipartFile file);

    Optional<User> findOneByUsername(String username);

    UserResponse getUserResponse(String username);
}
