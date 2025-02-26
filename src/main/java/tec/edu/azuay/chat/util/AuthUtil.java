package tec.edu.azuay.chat.util;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Objects;

public class AuthUtil {

    public static String extractJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.substring(7);
    }
}
