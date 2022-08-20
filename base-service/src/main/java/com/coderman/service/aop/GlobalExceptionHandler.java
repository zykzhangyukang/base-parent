package com.coderman.service.aop;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author coderman
 * @Title: 全局异常处理
 * @Description: TOD
 * @date 2022/3/519:01
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    public ResultVO<String> handleBusinessEx(BusinessException e){


        String message = e.getMessage();
        ResultVO<String> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_500);
        resultVO.setMsg(message);
        resultVO.setResult(null);
        return resultVO;
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultVO<String> handleMethodEx(HttpRequestMethodNotSupportedException e){


        ResultVO<String> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_500);
        resultVO.setMsg("请求方式不支持");
        resultVO.setResult(null);
        return resultVO;
    }


    @ExceptionHandler(value = Exception.class)
    public ResultVO<String> handleSystemEx(Exception e){


        ResultVO<String> resultVO = new ResultVO<>();
        resultVO.setCode(ResultConstant.RESULT_CODE_500);
        resultVO.setMsg("系统异常,请联系技术人员.");
        resultVO.setResult(null);
        return resultVO;
    }

}
