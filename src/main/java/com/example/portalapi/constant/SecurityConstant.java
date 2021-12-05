package com.example.portalapi.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 900_000; // 15min expressed in milliseconds
    public static final long REFRESH_EXPIRATION_TIME = 86_400_000; // 1 day expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String GET_ISSUER = "Portal Co.";
    public static final String GET_AUDIENCE = "Portal";
    public static final String ROLES = "roles";
    public static final String USER_ID = "user_id";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "Musisz się zalogować, aby uzyskać dostęp do tej strony";
    public static final String ACCESS_DENIED_MESSAGE = "Nie masz uprawnień dostępu do tej strony";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/api/authenticate", "/api/authenticate/**", "/api/token/refresh/", "/api/token/refresh/**", "/api/registration/**" };
}