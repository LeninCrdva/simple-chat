package tec.edu.azuay.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tec.edu.azuay.chat.models.dto.UserRequest;
import tec.edu.azuay.chat.service.auth.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody UserRequest userRequest) {

        return ResponseEntity.ok(authService.login(userRequest));
    }

    @PostMapping(value = {"/register"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> register(@RequestPart("userRequest") UserRequest userRequest, @RequestPart("file") MultipartFile file)  {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(userRequest, file));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getLoggedProfile() {
        return ResponseEntity.ok(authService.findLoggedInUser());
    }
}
