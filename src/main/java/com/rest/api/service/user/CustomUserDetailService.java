package com.rest.api.service.user;

import com.rest.api.exception.CustomUserNotFoundException;
import com.rest.api.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

//토큰에 세팅된 유저 정보로 회원정보를 조회
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    public UserDetails loadUserByUsername(String userPk){

        return userJpaRepository.findById(Long.valueOf(userPk)).orElseThrow(CustomUserNotFoundException::new);
    }
}