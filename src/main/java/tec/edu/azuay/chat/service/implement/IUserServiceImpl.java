package tec.edu.azuay.chat.service.implement;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import tec.edu.azuay.chat.exceptions.ExistsObjectException;
import tec.edu.azuay.chat.models.dto.AuthenticationResponse;
import tec.edu.azuay.chat.models.dto.UserRequest;
import tec.edu.azuay.chat.models.entity.User;
import tec.edu.azuay.chat.repository.IUserRepository;
import tec.edu.azuay.chat.service.interfaces.IUserService;
import tec.edu.azuay.chat.service.secondary.ImageUploadService;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IUserServiceImpl implements IUserService {

    private final ModelMapper modelMapper;

    private final IUserRepository userRepository;

    private final ImageUploadService uploadService;

    private final PasswordEncoder passwordEncoder;

    private User dtoToUser(UserRequest request) {
        return modelMapper.map(request, User.class);
    }

    private AuthenticationResponse entityToDto(User user) {
        return modelMapper.map(user, AuthenticationResponse.class);
    }

    @Override
    public User createOneUser(UserRequest newUser, MultipartFile file) {
        Optional<User> presentUser = findOneByUsername(newUser.getUsername());

        if (!ObjectUtils.isEmpty(presentUser)) {
            throw new ExistsObjectException();
        }

        Map<?, ?> map = uploadService.upload(file);
        String url = map.get("secure_url").toString();

        User user = dtoToUser(newUser);
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setImageUrl(url);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }
}
