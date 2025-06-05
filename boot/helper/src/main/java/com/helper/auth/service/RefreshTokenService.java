package com.helper.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helper.auth.mapper.RefreshTokenMapper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class RefreshTokenService {
	
	@Autowired
	private RefreshTokenMapper mapper;
	
	// DB에서 refresh 토큰 검증
	public Integer validateRefreshToken(String token) {
		log.info("----- RefreshTokenService validateRefreshToken() -----");
		return mapper.validateRefreshToken(token);
	}
}
