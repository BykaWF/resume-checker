package y.prozorov.resume_checker.util;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtUtil {
    public UUID extractUserId(Jwt jwt) {
        if (jwt == null || jwt.getClaimAsString("sub") == null) {
            throw new IllegalArgumentException("Invalid JWT: Missing subject (sub) claim");
        }
        return UUID.fromString(jwt.getClaimAsString("sub"));
    }
}