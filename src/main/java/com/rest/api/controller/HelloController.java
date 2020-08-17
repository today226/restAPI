package com.rest.api.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class HelloController {

    private static final String HELLO = "helloworld-nice to meet you";

    @Setter
    @Getter
    public static class Hello {
        private String message;
    }

    //1. 화면에 helloworld가 출력됩니다.
    @GetMapping(value = "/helloworld/string")
    @ResponseBody
    public String helloworldString() {
        return "helloworld";
    }

    //2. 화면에 {message:"helloworld"} 라고 출력됩니다.
    @GetMapping(value = "/helloworld/json")
    @ResponseBody
    public Hello helloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloworld";
        return hello;
    }

   //3. 화면에 helloworld.ftl의 내용이 출력됩니다.
   @GetMapping(value = "/helloworld/page")
   public String helloworld() {
       return "helloworld";
   }

    @GetMapping("/helloworld/long-process")
    @ResponseBody
    public String pause() throws InterruptedException {
        Thread.sleep(10000);
        return "Process finished";
    }
}