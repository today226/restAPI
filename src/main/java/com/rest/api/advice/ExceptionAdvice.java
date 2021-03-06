package com.rest.api.advice;

import com.rest.api.exception.CustomEmailSigninFailedException;
import com.rest.api.exception.CustomUserNotFoundException;
import com.rest.api.model.response.common.CommonResult;
import com.rest.api.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

//@RestControllerAdvice
//예외 발생 시 json형태로 결과를 반환하려면  annotation에 추가로 패키지를 적용하면 위에서 설명한 것처럼 특정 패키지 하위의 Controller에만 로직이 적용되게도 할 수 있다
//ex) @RestControllerAdvice(basePackages = “com.rest.api”)
//현재는 아무것도 적용하지 않아 프로젝트의 모든 Controller에 로직이 적용
//이러한 특성을 이용하면 @ControllerAdvice와 @ExceptionHandler를 조합하여 예외 처리를 공통 코드로 분리하여 작성

//@RequiredArgsConstructor
//초기화 되지 않은 final 필드와 @NonNull 어노테이션이 붙은 필드에 대한 생성자 생성, 주로 의존성 주입(Dependency Injection) 편의성을 위해서 사용
//https://medium.com/webeveloper/requiredargsconstructor-%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9D%98%EC%A1%B4%EC%84%B1-%EC%A3%BC%EC%9E%85-dependency-injection-4f1b0ac33561
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;
    /*
    @ExceptionHandler(Exception.class)
    Exception이 발생하면 해당 Handler로 처리하겠다고 명시하는 annotation이다.
    괄호안에는 어떤 Exception이 발생 할 때 handler를 적용할 것인지 Exception Class를 인자로 넣는다.
    예제에서는 Exception.class를 지정하였는데 Exception.class는 최상위 예외처리 객체이므로 다른 ExceptionHandler에서 걸러지지 않은 예외가 있으면 최종으로 이 handler를 거쳐 처리.
    그래서 메서드 명도 defaultException이라 명명하였습니다
     */
    /*
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Exception이 발생하면 Response에 출력되는 HttpStatus Code가 500으로 내려가도록 설정
    참고로 성공 시엔 HttpStatus code가 200으로 전달.
    HttpStatus Code의 역할은 성공이냐(200) 아니냐 정도의 의미만 있고 실제 사용하는 성공 실패 여부는 json으로 출력되는 정보를 이용
     */

//새롭게 만든 CustomUserNotFoundException이 정상적으로 동작되는지 테스트 하기 위해 임시 주석처리
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    protected CommonResult defaultException(HttpServletRequest request, Exception e) {

        /*
        Exception 발생시 이미 만들어둔 CommonResult의 실패 결과를 json 형태로 출력하도록 설정
        위에서 세팅한 HttpStatus Code외에 추가로 api 성공 실패여부를 다시 세팅하는 이유는 상황에 따라 다양한 메시지를 전달하기 위해서
        HttpStatus Code는 이미 고정된 스펙이기 때문에 (예 200 == OK, 404 == Not Found 등등…) 상세한 예외 메시지 전달에 한계가 있다.
        예를 들자면 “회원 정보가 없음” 이라는 에러 메시지는 HttpStatus Code상에 존재하지 않아 표현할 수가 없다.
        따라서 커스텀 Exception을 정의하고 해당 Exception 발생하면 적절한 형태의 오류 메시지를 담은 Json을 결과에 내리도록 처리 한 것
         */
//        return responseService.getFailResult();
//    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletRequest request, Exception e) {

        //예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.message"));
    }


    @ExceptionHandler(CustomUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletRequest request, CustomUserNotFoundException e) {

        // 예외 처리의 메시지를 MessageSource에서 가져오도록 수정
        return responseService.getFailResult(Integer.valueOf(getMessage("unKnown.code")), getMessage("unKnown.message"));
    }

    //code정보에 해당하는 메시지를 조회
    private String getMessage(String code) {

        return getMessage(code, null);
    }

    //code 정보, 추가 argument로 현재 locale에 맞는 에러 메시지를 조회
    private String getMessage(String code, Object[] args) {

        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    //CustomEmailSigninFailedException 발생 시 이에일 인증 실패 메세지 처리
    @ExceptionHandler(CustomEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailed(HttpServletRequest httpServletRequest, CustomEmailSigninFailedException e) {

        return responseService.getFailResult(Integer.valueOf(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.message"));
    }

    //CustomAuthenticationEntryPointException 발생시 메세지 처리
    @ExceptionHandler(CustomAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletRequest httpServletRequest, CustomAuthenticationEntryPointException e) {

        return responseService.getFailResult(Integer.valueOf(getMessage("entryPointException.code")), getMessage("entryPointException.message"));
    }

    //AccessDeniedException 발생 시 권한 없음 메세리 처리
    @ExceptionHandler(AccessDeniedException.class)
    public CommonResult AccessDeniedException(HttpServletRequest request, AccessDeniedException e) {
        return responseService.getFailResult(Integer.valueOf(getMessage("accessDenied.code")), getMessage("accessDenied.message"));
    }
}
