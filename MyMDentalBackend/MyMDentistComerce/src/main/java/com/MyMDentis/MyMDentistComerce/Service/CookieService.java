package com.MyMDentis.MyMDentistComerce.Service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public void addHttpOnlyCookie(String name, String value, int maxAge, HttpServletResponse response) {
        response.addHeader("Set-Cookie",
                name + "=" + value +
                        "; Max-Age=" + maxAge +
                        "; Path=/" +
                        "; HttpOnly" +
                        "; SameSite=Lax");
    }



    public void deleteCookie(String username, HttpServletResponse response) {
        Cookie cookie=new Cookie(username, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }






}
