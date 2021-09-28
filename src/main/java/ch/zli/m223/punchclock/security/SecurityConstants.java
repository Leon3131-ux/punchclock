package ch.zli.m223.punchclock.security;

public interface SecurityConstants {

    String SECRET = "EpicSecret";
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";

    String SIGN_UP_URL = "/api/signup";
    String LOGIN_URL = "/api/login";
    int EXPIRATION_TIME = 864000000;

}
