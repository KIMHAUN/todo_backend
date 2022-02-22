package com.ntels.demo.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.ntels.demo.model.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
public class TokenProvider {
	private static final String SECRET_KEY = "NMA8JPctFuna59f5";
	
	//JWT라이브러리를 이용해 JWT토큰을 생성한다.
	public String create(UserEntity userEntity) {
		//기한은 지금부터 1일로 설정
		Date expireDate = Date.from(
				Instant.now()
				.plus(1, ChronoUnit.DAYS));
		
		return Jwts.builder()
				//header에 들어갈 내용 및 서명을 하기위한 SECRET_KEY
				.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
				//payload에 들어갈 내용
				.setSubject(userEntity.getId()) //sub
				.setIssuer("demo app") //iss
				.setIssuedAt(new Date()) //iat
				.setExpiration(expireDate) //exp
				.compact();
	}
	
	//토큰을 디코딩 및 파싱하고 토큰의 위조 여부 검사.
	public String validateAndGetUserId(String token) {
		//parseClaimsJws메서드가 Base64로 디코딩 및 파싱
		//헤더와 페이로드를 setSignKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
		//위조되지 않았다면 페이로드(Claims)리턴, 위조라면 예외를 날림.
		//그 중 우리는 userId가 필요하므로 getBody를 부른다.
		Claims claims = Jwts.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
		
	}
}
