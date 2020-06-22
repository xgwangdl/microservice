package com.accenture.common.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.accenture.common.exception.RestException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class Sign {

	private static Map<String, JWTVerifier> verifierMap = new HashMap<>();
	private static Map<String, Algorithm> algorithmMap = new HashMap<>();

	private static Algorithm getAlgorithm(String secret) {
		Algorithm algorithm = algorithmMap.get(secret);
		if (algorithm == null) {
			synchronized (algorithmMap) {
				algorithm = algorithmMap.get(secret);
				if (algorithm == null) {
					algorithm = Algorithm.HMAC256(secret);
					algorithmMap.put(secret, algorithm);
				}
			}
		}
		return algorithm;
	}

	public static String getToken(String userName, String secret) {
		if (StringUtils.isEmpty(secret)) {
			throw new RestException("00X", "No signing token!");
		}
		Algorithm algorithm = getAlgorithm(secret);
		String token = JWT.create().withClaim("userName", userName).withExpiresAt(new Date(System.currentTimeMillis() + 600000))
				.sign(algorithm);
		return token;
	}
	
	public static DecodedJWT verifyToken(String token,String secret) {
		JWTVerifier verifier = verifierMap.get(token);
		if (verifier == null) {
			synchronized (verifierMap) {
				verifier = verifierMap.get(token);
				if (verifier == null) {
					Algorithm algorithm = getAlgorithm(secret);
					verifier = JWT.require(algorithm).build();
					verifierMap.put(token, verifier);
				}
			}
		}
		DecodedJWT decodedJWT = verifier.verify(token);
		if (decodedJWT == null) {
			throw new RestException("00Z", "Token is error!");
		}
		return decodedJWT;
	}
}
