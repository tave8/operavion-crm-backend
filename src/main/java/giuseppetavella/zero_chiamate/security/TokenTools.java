package giuseppetavella.zero_chiamate.security;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;


@Component
public class TokenTools {

    private final String secret;

    public TokenTools(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }


    // default: 7 days
    public String generateToken(String subject) {
        return generateToken(subject, Duration.ofDays(7));
    }

    // explicit duration
    public String generateToken(String subject, Duration expiry) {
        var now       = new Date();
        var expiresAt = new Date(now.getTime() + expiry.toMillis());
        var secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .issuedAt(now)
                .expiration(expiresAt)
                .subject(subject)
                .signWith(secretKey)
                .compact();
    }
    

    public void verifyToken(String token) throws UnauthorizedException {

        try {
            var secretKey = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parse(token);
        
        } catch (Exception ex) {
            throw new UnauthorizedException("Token is invalid.");
        }
    }


    public String extractSubjectFromToken(String token) {
        return Jwts.parser()
                        .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject();
    }

}