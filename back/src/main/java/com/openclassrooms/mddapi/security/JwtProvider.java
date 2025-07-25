package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret; // Secret key used to sign JWT tokens

    /**
     * Generates a JWT token for a given user.
     * @param userDetails the user's details
     * @return the generated JWT token as a String
     */
    public String generateToken(UserDetails userDetails, String identifierType) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("identifierType", identifierType)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(
                        Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String extractIdentifierType(String token) {
        return extractClaim(token, claims -> claims.get("identifierType", String.class));
    }

    /**
     * Checks if the provided token is valid for the given user.
     * @param token the JWT token as a String
     * @param userDetails the user's details to compare against
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks whether the token is expired.
     * @param token the JWT token as a String
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Validates the token against the provided user's details.
     * @param token the JWT token as a String
     * @param userDetails the user's details to compare against
     * @return true if the token is valid, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extracts the username (subject) from the JWT token.
     * @param token the JWT token as a String
     * @return the username contained in the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using a resolver function.
     * @param token the JWT token as a String
     * @param claimsResolver a function to resolve the desired claim
     * @param <T> the type of the claim to be returned
     * @return the value of the claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * @param token the JWT token as a String
     * @return a Claims object containing all the claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))) // Set the signing key for verification
                .build()
                .parseClaimsJws(token) // Parse the token
                .getBody(); // Return the body, which contains the claims
    }
}