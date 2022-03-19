package com.ntels.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ntels.demo.model.UserEntity;
import com.ntels.demo.persistence.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public UserEntity create(final UserEntity userEntity) {
		if (userEntity == null || userEntity.getEmail() == null) {
			throw new RuntimeException("Invalid arguments");
		}
		final String email = userEntity.getEmail();
		
		if (userRepository.existsByEmail(email)) {
			log.warn("Email already exists {}", email);
			throw new RuntimeException("Email already exists");
		}
		
		return userRepository.save(userEntity);
	}
	
	public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
		final UserEntity originalUser = userRepository.findByEmail(email);
		
		System.out.println("user password: " + originalUser.getPassword());
		System.out.println("input password: " + password);
		System.out.println("match: " + encoder.matches(password, originalUser.getPassword()));
		
		//matches 메서드를 이용해 패스워드가 같은지 확인
		if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
		
		//return userRepository.findByEmailAndPassword(email, password);
		return null;
	}
}
