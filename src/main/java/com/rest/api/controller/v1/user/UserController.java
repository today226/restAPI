package com.rest.api.controller.v1.user;

//Jpa Repository를 사용하는 Controller

import com.rest.api.entity.User;
import com.rest.api.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor    //class상단에 선언하면 class내부에 final로 선언된 객체에 대해서 Constructor Injection을 수행. 해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 된다
@RestController             //결과값을 JSON으로 출력
@RequestMapping(value ="/v1")
public class UserController {

    private final UserJpaRepository userJpaRepository;

    @GetMapping(value ="/user")
    public List<User> findAllUser(){

        return userJpaRepository.findAll();
    }

    @PostMapping(value ="/user")
    public User inserUser() {

        User user = User.
                builder().
                uid("today226@nate.com").
                name("관리자").
                password("okwonwoo82").
                build();

        return userJpaRepository.save(user);
    }
}
