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
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log in to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String[] PUBLIC_URLS = { "/authenticate", "/authenticate/**", "/token/refresh/**", "/registration/**" };
    //public static final String[] PUBLIC_URLS = { "**" };
}