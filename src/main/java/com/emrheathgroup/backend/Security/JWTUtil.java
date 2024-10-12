package com.emrheathgroup.backend.Security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTUtil {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;

	@Value("${application.security.jwt.expiration}")
	private long expirationTime;

//	public String generateToken(String userName) {
//		return Jwts.builder().setSubject(userName).setIssuedAt(new Date())
//				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
//				.signWith(SignatureAlgorithm.HS256, secretKey).compact();
//	}
//
//	public String extractUsername(String token) {
//		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//	}
//
//	public boolean validateToken(String token, String userName) {
//		return (userName.equals(extractUsername(token)) && !isTokenExpired(token));
//	}
//
//	private boolean isTokenExpired(String token) {
//		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration()
//				.before(new Date());
//	}
}
