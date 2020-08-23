package com.rest.api.repository;

import com.rest.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//생성한 entity를 이용해서 Table에 질의를 요청하기 위한 Repository를 생성
public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String email); //회원 가입 시 가입한 이메일 조회를 위해 다음 메서드를 선언
}
