package com.sunil.utilis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.sunil.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private String SECRET_KEY = "MySuperSecretKeyForJWTMustBeAtLeast32Bytes!";
    private int jwtExpirationMs = (1000 * 60 * 60);

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(User user) {
	    Map<String, Object> claimsMap = new HashMap<>();
	    claimsMap.put("userId", user.getId());
	    claimsMap.put("userName", user.getUserName());
	    claimsMap.put("roles", user.getRoles());
	    claimsMap.put("name", user.getName());
	    claimsMap.put("email", user.getEmail());
	    claimsMap.put("provider", user.getProvider());	    	    
	    return createToken(claimsMap, user.getUserName());
	}


    public String createToken(Map<String, Object> claimsMap, String userName) {
	    return Jwts.builder()
	            .setClaims(claimsMap)                           // 🧾 Custom Claims
	            .setSubject(userName)                           // 👤 subject = userName
	            .setIssuedAt(new Date(System.currentTimeMillis())) // 🕒 Issue time
	            .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // ⏳ Expiry
	            .signWith(getSigningKey())                      // 🔐 Signing with secret key
	            .compact();                                     // 📦 Final token
	}


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

}
