package tec.edu.azuay.chat.service.auth;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tec.edu.azuay.chat.exceptions.ObjectNotFoundException;
import tec.edu.azuay.chat.models.dto.AuthenticationResponse;
import tec.edu.azuay.chat.models.dto.TokenRefreshRequest;
import tec.edu.azuay.chat.models.dto.UserRequest;
import tec.edu.azuay.chat.models.dto.UserResponse;
import tec.edu.azuay.chat.models.entity.User;
import tec.edu.azuay.chat.service.interfaces.IUserService;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUserService userService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;

    public AuthenticationResponse registerUser(UserRequest newUser, MultipartFile file) {
        User user = userService.createOneUser(newUser, file);

        String accessToken = jwtService.getAccessToken(user, generateExtraClaims(user));
        String refreshToken = jwtService.getRefreshToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public UserResponse userToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());
        extraClaims.put("userId", user.getId());

        return extraClaims;
    }

    public AuthenticationResponse login(UserRequest requestAuthentication) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(requestAuthentication.getUsername(), requestAuthentication.getPassword());

        authenticationManager.authenticate(authentication);

        User user = userService.findOneByUsername(requestAuthentication.getUsername()).orElseThrow(() -> new ObjectNotFoundException("User not found"));

        String accessToken = jwtService.getAccessToken(user, generateExtraClaims(user));
        String refreshToken = jwtService.getRefreshToken(user, generateExtraClaims(user));

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public AuthenticationResponse refreshToken(TokenRefreshRequest refreshToken) {
        User userDetails = userService.findOneByUsername(jwtService.extractUsername(refreshToken.getRefreshJwt())).get();

        if (validate(refreshToken.getRefreshJwt())) {
            String newAccessToken = jwtService.getAccessToken(userDetails, generateExtraClaims(userDetails));

            AuthenticationResponse authRsp = new AuthenticationResponse();
            authRsp.setAccessToken(newAccessToken);
            authRsp.setRefreshToken(refreshToken.getRefreshJwt());

            return authRsp;
        }

        return null;
    }

    public boolean validate(String jwt) {

        try {
            jwtService.extractUsername(jwt);

            return true;
        } catch (Exception e) {
            Logger.getGlobal().info(e.getMessage());
            return false;
        }
    }

    public UserResponse findLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = (String) auth.getPrincipal();

        return userToUserResponse(userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found by username: " + username)));
    }

}
