package com.example.Team.Task.Manager.security;



import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import java.util.Date;


@Component
public class JwtCore {
   @Value("${Team-Task-Manager.app.secret}")
    private  String secret;

    @Value("${Team-Task-Manager.app.lifetime}")
    private int lifeTime;



    public String generateToken(Authentication authentication){

        UserDetailsImpl userDetails =(UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + lifeTime))
                .signWith(SignatureAlgorithm.HS256,secret)
                .compact();
    }

    public String getNameFromJwt(String token){

        return Jwts.parser().setSigningKey(secret).build().parseSignedClaims(token).getBody().getSubject();
    }



}
