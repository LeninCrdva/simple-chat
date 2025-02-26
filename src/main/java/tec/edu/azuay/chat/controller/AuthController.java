package tec.edu.azuay.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tec.edu.azuay.chat.models.dto.AuthenticationResponse;
import tec.edu.azuay.chat.models.dto.TokenRefreshRequest;
import tec.edu.azuay.chat.models.dto.UserRequest;
import tec.edu.azuay.chat.service.auth.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(authService.login(userRequest));
    }

    @PreAuthorize("permitAll()")
    @PostMapping(value = {"/register"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@RequestPart("userRequest") UserRequest userRequest, @RequestPart("file") MultipartFile file)  {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userRequest, file));
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody TokenRefreshRequest refreshToken){
        AuthenticationResponse rsp = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(rsp);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getLoggedProfile() {
        return ResponseEntity.ok(authService.findLoggedInUser());
    }
}
