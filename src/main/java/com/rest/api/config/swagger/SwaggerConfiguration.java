package com.rest.api.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//localhost:8080/swagger-ui.html api 확인
@Configuration      //1개 이상 Bean을 등록하고 있음을 명시하는 어노테이션
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerApi() {

        //PathSelectors.ant(“/v1/**”) swaggerInfo를 세팅하면 문서에 대한 설명과 작성자 정보를 노출시킬 수 았다
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage(("com.rest.api.controller"))).paths(PathSelectors.any()) //com.rest.api.controller 하단의 Controller 내용을 읽어 mapping 된 resource들을 문서화
                .build()
                .useDefaultResponseMessages(false); //기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("Spring API Documentation")
                .description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다")
                .license("denis")
//              .contact(new Contact("Bamdule Swagger", "https://bamdule.tistory.com/", "Bamdule5@gmail.com"))
                .version("1.0")
                .build();
    }
}