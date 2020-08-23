package com.rest.api.config.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfiguration {

    /*
    해당 메소드를 빈으로 등록 안하면 org.springframework.security.crypto.password.PasswordEncoder에서  passwordEncoder 메소드를 참조
    5.x 부터 암호화 설정 방식을 변경하였다. 다양한 암호화 방식을 실시간으로 반영하기 위해 기존에 암호화된 값을 새로운 암호화 방식으로 변경하는 것이 쉽지 않았다. 그리고 sha 알고리즘은 deprecated 되었다
     */
    @Bean
    public PasswordEncoder passwordEncoder(){

        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
