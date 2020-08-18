package com.rest.api.controller.v1.user;

import com.rest.api.entity.User;
import com.rest.api.exception.CustomUserNotFoundException;
import com.rest.api.model.response.common.CommonResult;
import com.rest.api.model.response.result.ListResult;
import com.rest.api.model.response.result.SingleResult;
import com.rest.api.repository.UserJpaRepository;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//Jpa Repository를 사용하는 Controller
@Api(tags = {"1. User"})    //UserController를 대표하는 최상단 타이틀 영역에 표시될 값을 세팅
@RequiredArgsConstructor    //class상단에 선언하면 class내부에 final로 선언된 객체에 대해서 Constructor Injection을 수행. 해당 어노테이션을 사용하지 않고 선언된 객체에 @Autowired를 사용해도 된다
@RestController             //결과값을 JSON으로 출력
@RequestMapping(value ="/v1")
public class UserController {

    private final UserJpaRepository userJpaRepository;  //DB 처리를 위한 Jpa 서비스
    private final ResponseService responseService;      //결과를 처리할 서비스

    @ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다")   //ApiOperation(value = “회원 조회”, notes = “모든 회원을 조회한다”) 각각의 resource에 제목과 설명을 표시하기 위해 설정
    @GetMapping(value = "/users")
    public ListResult<User> findAllUser(){

        //결과 데이터가 여러건인 경우 getListResult를 이용해서 결과를 출력
        return responseService.getListResult(userJpaRepository.findAll());
    }

    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
    @GetMapping(value = "/user/{userId}")
    public SingleResult<User> findUserById(@ApiParam(value = "회원ID",    required        = true) @PathVariable long userId,
                                           @ApiParam(value = "언어",      defaultValue    = "ko") @RequestParam String lang) throws Exception {

        //결과 데이터가 단일건인 경우 getBasicResult를 이용해서 결과를 출력
        return responseService.getSingleResult(userJpaRepository.findById(userId).orElseThrow(CustomUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다")
    @PostMapping(value ="/user")
    public SingleResult<User> save(@ApiParam(value = "회원아이디",    required = true) @RequestParam String uid,
                                   @ApiParam(value = "회원이름",      required = true) @RequestParam String name,
                                   @ApiParam(value = "비밀번호",      required = true) @RequestParam String password) {

        User user = User.
                builder().
                uid(uid).
                name(name).
                password(password).
                build();

        return responseService.getSingleResult(userJpaRepository.save(user));
    }

    @ApiOperation(value = "회원 수정", notes = "회원 정보를 수정한다")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(@ApiParam(value = "회원번호",   required = true) @RequestParam long msrl,
                                     @ApiParam(value = "회원아이디", required = true) @RequestParam String uid,
                                     @ApiParam(value = "회원이름",   required = true) @RequestParam String name,
                                     @ApiParam(value = "비밀번호",   required = true) @RequestParam String password) {
        //추후에 비밀번호만 처리 안해도 업데이트 할 수 있게 개발 할 것
        User user = User.builder().
                msrl(msrl).
                uid(uid).
                name(name).
                password(password).
                build();

        return responseService.getSingleResult(userJpaRepository.save(user));
    }

    @ApiOperation(value = "회원 삭제", notes ="userId로 회원 정보를 삭제한다")
    @DeleteMapping(value = "/user/{msrl}")
    public CommonResult delete(@ApiParam(value = "회원번호", required = true) @PathVariable long msrl) {

        userJpaRepository.deleteById(msrl);
        //성공 결과 정보만 필요한 경우 getSuccessResult()를 이용하여 결과를 출력
        return responseService.getSuccessResult();
    }
}