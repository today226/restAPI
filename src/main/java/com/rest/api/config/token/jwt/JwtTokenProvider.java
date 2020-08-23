package com.rest.api.config.token.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

/*
Jwt 토큰 생성 및 유효성 검증을 하는 컴포넌트.
Jwt는 여러 가지 암호화 알고리즘을 제공하며 알고리즘(SignatureAlgorithm.XXXXX)과 비밀키(secretKey)를 가지고 토큰을 생성.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {     //JWT 토큰을 생성 및 검증 모듈

    @Value("spring.jwt.secret")     //변수에 프로퍼티값 매칭
    private String secretKey;
    private long tokenValidMilisecond = 1000L * 60 * 60;    //한시간만 토큰 유효
    /*
    UserDetailsService 인터페이스에는 DB에서 유저 정보를 불러오는 중요한 메소드가 존재한다.
    바로 loadUserByUsername() 메소드이다. 이 메소드에서 유저 정보를 불러오는 작업을 하면 된다.
    UserDetailsService 인터페이스를 구현하면 loadUserByUsername() 메소드가 오버라이드 될 것이다.
    여기에서 CustomUserDetails 형으로 사용자의 정보를 가져오면 된다.
    가져온 사용자의 정보를 유/무에 따라 예외와 사용자 정보를 리턴하면 된다.
    다시 한번 말하자면 이 부분은 DB에서 유저의 정보를 가져와서 리턴해주는 작업
     */
    private final UserDetailsService userDetailsService;

    @PostConstruct
    //객체의 초기화 부분, 객체가 생성된 후 별도의 초기화 작업을 위해 실행하는 메소드를 선언한다.@PostConstruct 어노테이션을 설정해놓은 init 메소드는 WAS가 띄워질 때 실행 https://yonguri.tistory.com/100
    protected void init() {

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //JWT 토큰 생성
    public String createToken(String userPk, List<String> roles) {

        Claims claims = Jwts.claims().setSubject(userPk);   //claim 정보에 회원을 구분할 수 있는 값을 세팅하였다가 토큰이 들어오면 해당 값으로 회원을 구분하여 리소스를 제공하면 된다
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder().
                setClaims(claims).                                              //데이터
                setIssuedAt(now).                                               //토큰 발행일자
                setExpiration(new Date(now.getTime() + tokenValidMilisecond)).  //setExpire Time(만료 시간)
                signWith(SignatureAlgorithm.HS256, secretKey).                  //암호화 알고리즘, secret값 셋팅
                compact();
    }

  /*
   UserDetails
   Spring Security에서 사용자의 정보를 담는 인터페이스는 UserDetails 인터페이스이다.
   우리가 이 인터페이스를 구현하게 되면 Spring Security에서 구현한 클래스를 사용자 정보로 인식하고 인증 작업을 한다.
   쉽게 말하면 UserDetails 인터페이스는 VO 역할을 한다고 보면 된다. 그래서 우리는 사용자의 정보를 모두 담아두는 클래스를 구현
   https://to-dy.tistory.com/86
    */

    //Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
}

    //Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token) {

        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /*
    resolveToken 메서드는 Http request header에 세팅된 토큰 값을 가져와 유효성을 체크.
    제한된 리소스를 요청할 때 Http header에 토큰을 세팅하여 호출하면 유효성을 검증하여 사용자 인증을 할 수 있다
     */
    //Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest request) {

        return request.getHeader("X-AUTH-TOKEN");
    }

    //Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {

        try{

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch(Exception e) {
            return false;
        }
    }
}
