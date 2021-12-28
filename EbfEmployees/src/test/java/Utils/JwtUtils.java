package Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.itekako.EbfEmployees.configurations.AuthConfiguration;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
public class JwtUtils {

    private final AuthConfiguration authConfiguration;

    public String generateAccessToken() {
        return JWT.create().withSubject(String.valueOf("testUser"))
                .withClaim("ROLE", "admin")
                .withExpiresAt(new Date(System.currentTimeMillis() + authConfiguration.getLifeTime()))
                .sign(Algorithm.HMAC512(authConfiguration.getSecret().getBytes()));
    }

    public String generateAccessTokenWrongSecret() {
        String secret = authConfiguration.getSecret() + "WRONG";
        return JWT.create().withSubject(String.valueOf("testUser"))
                .withClaim("ROLE", "admin")
                .withExpiresAt(new Date(System.currentTimeMillis() + authConfiguration.getLifeTime()))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public String generateAccessTokenExpired() {
        return JWT.create().withSubject(String.valueOf("testUser"))
                .withClaim("ROLE", "admin")
                .withExpiresAt(new Date(System.currentTimeMillis() - authConfiguration.getLifeTime()))
                .sign(Algorithm.HMAC512(authConfiguration.getSecret().getBytes()));
    }

    public String generateAccessWithoutRole() {
        return JWT.create().withSubject(String.valueOf("testUser"))
                .withExpiresAt(new Date(System.currentTimeMillis() + authConfiguration.getLifeTime()))
                .sign(Algorithm.HMAC512(authConfiguration.getSecret().getBytes()));
    }
}
