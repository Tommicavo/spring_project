package com.project.ecom.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import com.project.ecom.models.entities.RoleEntity;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.services.BlackTokenService;
import com.project.ecom.utils.AppLogger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtService {

    @Autowired
    private AppLogger logger;

    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Autowired
    private BlackTokenService blackTokenService;

    private static final int EXPIRATION_MILLIS = 1000 * 60 * 60; // 1 hour
    private static final String ROLES = "roles"; 

    /*
        NOTES:
        Jws       → Signed JWT (includes both claims + signature)
        Jwts      → Helper class for JWT parsing/creation
        claimsJws → Parsed JWT, ready for use
        Claims    → Payload (data) inside the JWT
    */

    public String createToken(UserEntity user) {
        String str_idUser = String.valueOf(user.getIdUser());
        Set<RoleEntity> roles = user.getRoles();
        Date creationDate = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_MILLIS);
        SecretKey key = getSigningKey();

        return Jwts.builder()
                   .subject(str_idUser)
                   .claim(ROLES, roles)
                   .issuedAt(creationDate)
                   .expiration(expirationDate)
                   .signWith(key)
                   .compact();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String token = null;
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        }
        return token;
    }

    public Map<Boolean, String> validateToken(String token) {
        Map<Boolean, String> validationResult = new HashMap<>();

        boolean isBlackToken = isBlackToken(token);
        if (isBlackToken) {
            validationResult.put(false, "BLACKLISTED TOKEN");
            return validationResult;
        }

        Claims claims = getTokenPayload(token);

        if (claims == null) {
            validationResult.put(false, "INVALID TOKEN");
            return validationResult;
        }

        Date expirationDate = claims.getExpiration();
        if (expirationDate.before(new Date())) {
            validationResult.put(false, "EXPIRED TOKEN");
            return validationResult;
        }

        validationResult.put(true, "TOKEN OK");
        return validationResult;
    }

    public String getIdFromToken(String token) {
        Claims claims = getTokenPayload(token);
        return claims != null ? claims.getSubject() : null;
    }

    public Date getExpirationDateFromToken(String token) {
        Claims claims = getTokenPayload(token);
        return claims != null ? claims.getExpiration() : null;
    }

    public String getRolesFromToken(String token) {
        Claims claims = getTokenPayload(token);
        return claims != null ? claims.get(ROLES).toString() : null;
    }

    private Claims getTokenPayload(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                                        .verifyWith(getSigningKey())
                                        .build()
                                        .parseSignedClaims(token);
            return claimsJws.getPayload();
        } catch (ExpiredJwtException e) {
            logger.log.error("EXPIRED TOKEN: " + e.getMessage());
            return e.getClaims();
        } catch (MalformedJwtException e) {
            logger.log.error("INVALID TOKEN: " + e.getMessage());
            return null;
        } catch (Exception e) {
            logger.log.error("TOKEN EXCEPTION: " + e.getMessage());
            return null;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private boolean isBlackToken(String token) {
        return blackTokenService.isTokenBlacklisted(token);
    }
}
