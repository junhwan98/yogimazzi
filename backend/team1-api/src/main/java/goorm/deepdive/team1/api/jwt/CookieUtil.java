package goorm.deepdive.team1.api.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;


public class CookieUtil {

    public static final int COOKIE_EXPIRED_AGE = 0;
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    private CookieUtil(){
    }

    public static Cookie createCookie(String cookieName, String data, int exp) {
        Cookie cookie = new Cookie(cookieName, data);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/"); 쿠키가 적용될 범위?
        cookie.setMaxAge(exp);
        return cookie;
    }

    public static String getRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "Refresh-Token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }


    public static void clearAuthCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_EXPIRED_AGE);
        response.addCookie(cookie);
    }
}
