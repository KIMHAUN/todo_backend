package com.ntels.demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ntels.demo.config.TokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			//요청에서 토큰 가져오기
			String token = parseBearerToken(request);
			log.info("Filter is running...");
			//토큰 검사하기. JWT이므로 인가 서버에 요청하지 않고도 검증 가능
			if (token != null && !token.equalsIgnoreCase("null")) {
				//userId 가져오기. 위조된 경우 예외 처리된다.
				String userId = tokenProvider.validateAndGetUserId(token);
				log.info("Authenticated user ID : " + userId);
				//인증 완료. SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
				AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userId, //AuthenticationPrincipal을 String으로 지정했기 때문에 사용 가능. 보통 UserDetails라는 오브젝트를 넣는데 우리는 넣지 않았다.
						null,
						AuthorityUtils.NO_AUTHORITIES);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
				securityContext.setAuthentication(authentication);
				//SecurityContextHolder은 기본적로 ThreadLocal 에 저장된다.
				SecurityContextHolder.setContext(securityContext);
			}
			
		} catch (Exception ex) {
			log.error("Could not ser user authentication in security context", ex);
		}
		filterChain.doFilter(request, response);
	}

	private String parseBearerToken(HttpServletRequest request) {
		//Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴하다.
		String bearerToken = request.getHeader("Authorization");
		
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}
	
}
