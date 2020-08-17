package com.rest.api.controller.v1.user;

//Jpa Repository를 사용하는 Controller

import com.rest.api.entity.User;
import com.rest.api.repository.UserJpaRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"1. User"})    //UserController를 대표하는 최상단 타이틀 영역에 표시될 값을 세팅
@RequiredArgsConstructor    //class상단에 선언하면 class내부에 final로 선언된 객체에 대해서 Constructor Injection을 수행. 해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 된다
@RestController             //결과값을 JSON으로 출력
@RequestMapping(value ="/v1")
public class UserController {

    private final UserJpaRepository userJpaRepository;

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다")   //ApiOperation(value = “회원 조회”, notes = “모든 회원을 조회한다”) 각각의 resource에 제목과 설명을 표시하기 위해 설정
    @GetMapping(value = "/user")
    public List<User> findAllUser(){

        return userJpaRepository.findAll();
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다")
    @PostMapping(value ="/user")
    public User save(@ApiParam(value = "회원아이디",    required = true) @RequestParam String uid,
                     @ApiParam(value = "회원이름",      required = true) @RequestParam String name,
                     @ApiParam(value = "비밀번호",      required = true) @RequestParam String password) {

        User user = User.
                builder().
                uid(uid).
                name(name).
                password(password).
                build();

        return userJpaRepository.save(user);
    }
}