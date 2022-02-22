package com.ntels.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import com.ntels.demo.security.JwtAuthenticationFilter;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	//web.xml대신 HttpSecurity를 이용해 시큐리티 관련 설정.
	protected void configure(HttpSecurity http) throws Exception {
		//http 시큐리티 빌더
		http.cors() //WebMvcConfig에서 이미 설정했으므로 기본 cors설정.
		.and()
		.csrf()
		.disable()
		.httpBasic()
		.disable()
		.sessionManagement() //세션 기반이 아님을 선언
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests() // /와 /auth/**경로는 인증 안해도 됨.
		.antMatchers("/", "/auth/**").permitAll()
		.anyRequest() // / 와 /auth/**이외의 모든 경로는 인증 해야 됨.
		.authenticated();
		
		//filter등록. 매 요청마다 CorsFilter실행 후에 jwtAuthenticationFilter실행.
		http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
	}
}
