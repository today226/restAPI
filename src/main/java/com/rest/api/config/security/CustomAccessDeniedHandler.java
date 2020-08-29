package com.rest.api.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*Jwt 토큰은 정상이라는 가정하에 Jwt 토큰이 가지지 못한 권한의 리소스를 접근할 때 발생하는 오류 처리하려면
SpringSecurity에서 제공하는 AccessDeniedHandler를 상송받아 커스터 마이징 해야 한다
*/
@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {

        httpServletResponse.sendRedirect("/exception/accessdenied");
    }
}
