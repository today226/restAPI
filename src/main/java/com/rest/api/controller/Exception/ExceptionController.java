package com.rest.api.controller.Exception;

import com.rest.api.advice.CustomAuthenticationEntryPointException;
import com.rest.api.model.response.common.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//
@RequiredArgsConstructor
@Controller
@RequestMapping(value ="/exception")
public class ExceptionController {

    @GetMapping(value ="/entrypoint")
    public CommonResult entrypointException(){

        throw new CustomAuthenticationEntryPointException();
    }
}
