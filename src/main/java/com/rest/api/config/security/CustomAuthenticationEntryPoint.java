package com.rest.api.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//온전한 Jwt가 전달이 안될 경우는 토큰 인증 처리 자체가 불가능하기 때문에, 토큰 검증 단에서 프로세스가 끝나버리게 된다. 해당 예외를 잡아내려면 SpringSecurity에서 제공하는 AuthenticationEntryPoint를 상속받아 재정의 해야된다
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException ex) throws IOException, ServletException {

        httpServletResponse.sendRedirect("/exception/entrypoint");
    }
}
