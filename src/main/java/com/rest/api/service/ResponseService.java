package com.rest.api.service;

import com.rest.api.model.response.common.CommonResponse;
import com.rest.api.model.response.common.CommonResult;
import com.rest.api.model.response.result.ListResult;
import com.rest.api.model.response.result.SingleResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service    //해당 Class가 Service임을 명시
public class ResponseService {

    public CommonResponse commonResponse;

    //단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {

        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    //다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {

        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    //성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() {

        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    //실패 결과만 처리하는 메소드
    public CommonResult getFailResult(int code, String message) {

        CommonResult result = new CommonResult();
        result.setSuccess(false);
        result.setCode(code);               //yml 파일의 설정된 Exception 에러 문구를 받는다
        result.setMessage(message);         //yml 파일의 설정된 Exception 에러 문구를 받는다
        return result;
    }

    //결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(CommonResult result) {

        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());
    }
}
