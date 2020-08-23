package com.rest.api.config.security;

import com.rest.api.config.token.jwt.JwtAuthenticationFilter;
import com.rest.api.config.token.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
extends WebSecurityConfigurerAdapter
SecurityConfig 클래스 설정
설정하는 순간 스프링 부트가 제공하는 스프링 시큐리티 자동설정은 더이상 제공되지 않음
하지만 WebSecurityConfigurerAdapter 클래스를 상속 받는 순간 모든 요청은 인증을 필요로 하게 된다
 */
//서버에 보안 설정을 적용합니다
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    /*
    authenticationManagerBean 메소드의 경우에는 Spring Security에서 사용되는 인증객체를 Bean으로 등록할 때 사용합니다. @Bean을 붙여서 Bean으로 등록
    다른 AuthorizationServer나 ResourceServer가 참조할 수 있도록 오버라이딩 해서 빈으로 등록
    Form Login 부분을 customizing을 시키는 경우가 많기 때문에 AuthenticationManager를 Bean으로 등록시켜두면 두고두고 편한 일들이 많다
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {

        return super.authenticationManagerBean();
    }

    //설정에 관한 좀 더 자세한 사항은 추후에 https://wedul.site/170 참조 할 것
    //메소드를 재정의하여 로그인 URL, 권한분리, logout URL 등등을 설정할 수 있다. (자세한 설명은 메서드에 주석으로 확인)
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                    .httpBasic().disable()                                                              //rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                    .csrf().disable()                                                                   //rest api이므로 csrf 보안이 필요없으므로 disable처리.
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)         //jwt token으로 인증하므로 세션은 필요없으므로 생성안함.
                    .and()
                    .authorizeRequests()                                                                //다음 리퀘스트에 대한 사용권한 체크
                    .antMatchers("/*/signin", "/*/signup").permitAll()                      //가입 및 인증 주소는 누구나 접근가능
                    .antMatchers(HttpMethod.GET, "helloworld/**").permitAll()               //hellowworld로 시작하는 GET요청 리소스는 누구나 접근가능
                    .anyRequest().hasRole("USER")                                                        //그외 나머지 요청은 모두 인증된 회원만 접근 가능
                    .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); //jwt token 필터를 id/password 인증 필터 전에 넣는다
    }

    /*
    메서드를 재정의하여 로그인 상관 없이 허용을 해줘야할 리소스 위치를 정의한다.
    HTTP를 적용하기 전에 시큐리티 필터를 적용할지 말지를 먼저 결정
    스프링 시큐리티를 필터를 사용하기 전에 적용된 패턴을 다 걸러냄
    서버가 일을 조금이라도 일을 덜하게 하기 위해 정적인 리소스들은 웹 필터로 걸러주는 것을 권장
    */
    @Override                                                                           //ignore check swagger resource
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }
}
