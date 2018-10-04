package com.example.HttpSession.Services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtTokenGeneratorService {

    public String generateJwtToken (String userName) throws UnsupportedEncodingException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder().setId(userName).setIssuedAt(now).signWith(signatureAlgorithm, "secret".getBytes("UTF-8"));
        return jwtBuilder.compact();
    }

    public String decodeJwtToken (String jwtToken) throws UnsupportedEncodingException {
        Claims claims = Jwts.parser().setSigningKey("secret".getBytes("UTF-8")).parseClaimsJws(jwtToken).getBody();
        return claims.getId();
    }
}
