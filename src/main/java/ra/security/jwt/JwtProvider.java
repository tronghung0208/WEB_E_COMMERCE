package ra.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ra.security.user_principle.UserPrinciple;

import java.util.Date;
@Component
public class JwtProvider {

    private Logger logger= LoggerFactory.getLogger(JwtEntryPoint.class);
    @Value("${expired}")
    private Long EXPIRED;
    @Value("${secret_key}")
    private String SECRET_KEY;

    public String generateToken(UserPrinciple userPrinciple) {
        String token = Jwts.builder()
                .setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRED))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
        return token;
    }

    public Boolean validationToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expiredJwtException) {
            logger.error("Expired Token {}", expiredJwtException.getMessage());
        } catch (MalformedJwtException malformedJwtException) {
            logger.error("Invalid format {}", malformedJwtException.getMessage());
        } catch (UnsupportedJwtException unsupportedJwtException) {
            logger.error("Unsupported token {}", unsupportedJwtException.getMessage());
        } catch (SignatureException signatureException) {
            logger.error("Invalid Signature token {}", signatureException.getMessage());
        }
        return false;
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }
}
