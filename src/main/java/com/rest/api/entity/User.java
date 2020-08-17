package com.rest.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
public class User {

    //JPA의 기본키 전략에 대해서는 아래 링크에 자세히 설명되어 있다 추후에 참조. https://feco.tistory.com/96
    @Id                 //primary key임을 알림
    @GeneratedValue(strategy = GenerationType.IDENTITY)     //pk생성전략을 DB에 위임한다는 의미입니다. mysql로 보면 pk 필드를 auto_increment로 설정해 놓은 경우로 보면 된다
    private long msrl;

    @Column(nullable = false, unique = true, length = 30)   //uid column을 명시. 필수이고 유니크한 필드이며 길이는 30
    private String uid;

    @Column(nullable = false, length = 100)                 //name column을 명시. 필수이고 길이는 100.
    private String password;

    @Column(nullable = false, length = 100)                 //name column을 명시. 필수이고 길이는 100
    private String name;

}
