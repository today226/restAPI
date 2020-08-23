package com.rest.api.controller.v1.user;

import com.rest.api.config.token.jwt.JwtTokenProvider;
import com.rest.api.entity.User;
import com.rest.api.exception.CustomEmailSigninFailedException;
import com.rest.api.model.response.result.SingleResult;
import com.rest.api.model.response.common.CommonResult;
import com.rest.api.repository.UserJpaRepository;
import com.rest.api.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

//로그인 성공 시에는 결과로 Jwt토큰을 발급
@Api(tags= {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class SignController {

    private final UserJpaRepository userJpaRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다.")
    @PostMapping(value = "/signin")
    public SingleResult<String> signin(@ApiParam(value = "회원ID : 이메일", required = true) @RequestParam String id ,
                                       @ApiParam(value = "비밀번호",        required = true) @RequestParam String password) {

        User user = userJpaRepository.findByUid(id).orElseThrow(CustomEmailSigninFailedException::new);

        if(!passwordEncoder.matches(password, user.getPassword())) throw new CustomEmailSigninFailedException();

        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @ApiOperation(value = "가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/signup")
    public CommonResult signin(@ApiParam(value = "회워ID : 이메일",  required = true) @RequestParam String id,
                               @ApiParam(value = "비밀번호",         required = true) @RequestParam String password,
                               @ApiParam(value = "이름",            required = true) @RequestParam String name) {

        userJpaRepository.save(User.builder().
                uid(id).
                password(passwordEncoder.encode(password)). //가입 시에는 패스워드 인코딩을 위해 passwordEncoder설정을 합니다. 기본 설정은 bcrypt encoding이 사용.
                name(name).
                roles(Collections.singletonList("ROLE_USER")).
                build());

        return responseService.getSuccessResult();
    }
}