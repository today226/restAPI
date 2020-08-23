package com.rest.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/*
entity는 db table 간의 구조와 관계를 JPA가 요구하는 형태로 만든 model
테이블에 있는 칼럼 값들의 정보, 테이블 간의 연관 관계(1:N, N:1 등) 정보를 담고 있다
 */
//JPA의 기본키 전략에 대해서는 아래 링크에 자세히 설명  https://feco.tistory.com/96
@Builder                //해당 클래스의 빌드패턴 클래스를 생성, 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함 https://johngrib.github.io/wiki/builder-pattern/ https://mommoo.tistory.com/54
@Entity                 //jpa entity
@Getter
@NoArgsConstructor      //인자없는 생성자를 자동으로 생성
@AllArgsConstructor     //인자를 모두 갖춘 생성자를 자동으로 생성
@Table(name = "user")   //user 테이블과 매핑됨을 명시
public class User implements UserDetails {

    //JPA의 기본키 전략에 대해서는 아래 링크에 자세히 설명되어 있다 추후에 참조. https://feco.tistory.com/96
    @Id                                                     //primary key임을 알림
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //pk생성전략을 DB에 위임한다는 의미입니다. mysql로 보면 pk 필드를 auto_increment로 설정해 놓은 경우로 보면 된다
    private long msrl;

    @Column(nullable = false, unique = true, length = 30)   //uid column을 명시. 필수이고 유니크한 필드이며 길이는 30
    private String uid;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  //Json결과로 출력 안 할 데이터는 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) 어노테이션을 선언
    @Column(nullable = false, length = 100)                 //name column을 명시. 필수이고 길이는 100.
    private String password;

    @Column(nullable = false, length = 100)                 //name column을 명시. 필수이고 길이는 100
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    //roles는 회원이 가지고 있는 권한 정보이고, 가입했을 때는 기본 “ROLE_USER”가 세팅
    //권한은 회원당 여러 개가 세팅될 수 있으므로 Collection으로 선언
    //UserDetails로 부터 상속 받은 메소드들
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() { //getUsername은 security에서 사용하는 회원 구분 id입니다. 여기선 uid로 변경

        return this.uid;
    }

    //아래의 메소드들은 값들은 Security에서 사용하는 회원 상태 값입니다. 여기선 모두 사용 안 하므로 true로 설정
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {  //계정이 만료가 안되었는지

        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {   //계정이 잠기지 않았는지

        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {//계정 패스워드가 만료 안됬는지

        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {        //계정이 사용 가능한지

        return true;
    }
}
