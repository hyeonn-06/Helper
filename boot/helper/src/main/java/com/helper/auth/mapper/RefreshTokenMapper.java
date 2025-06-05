package com.helper.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RefreshTokenMapper {
	public Integer validateRefreshToken(String token);
}
