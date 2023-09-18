package com.emintufan.orderservice.security.service;

import com.emintufan.orderservice.security.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        Claims claim = extractAllClaims(token);
        return (claimResolver.apply(claim));
    }

    private Claims extractAllClaims(String token) {
        return (Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody());
    }

    public boolean isTokenValid(String token, UserDetails userDetails)
    {
        return (!isTokenExpired(token) && extractUserName(token).equals(userDetails.getUsername()));
    }
    private boolean isTokenExpired(String token)
    {
        return (extractExpiration(token).before(new Date()));
    }
    private Date extractExpiration(String token)
    {
        return extractClaim(token,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(HashMap<String, Object> extraClaims, UserDetails userDetails) {

        List<String> authorities = userDetails.getAuthorities().stream().
                map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return (Jwts.builder()
                .claim("authorities", authorities)
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, signKey())
                .compact());
    }

    private Key signKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SecurityConstants.JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
