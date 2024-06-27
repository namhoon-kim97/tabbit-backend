package com.jungle.Tabbit.global.config.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtConst {
    ACCESS_TOKEN_EXPIRE_TIME(Integer.MAX_VALUE),
    REFRESH_TOKEN_EXPIRE_TIME(3600000 * 24);

    private final long time;
}
