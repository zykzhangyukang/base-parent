package com.coderman.service.aop;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author coderman
 * @Title: 全局异常处理
 * @Description: TOD
 * @date 2022/3/519:01
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseService {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultVO<Void> handlerException(HttpServletRequest request, Exception e) {


        ResultVO<Void> resultVO = new ResultVO<>();

        if (e instanceof BusinessException) {

            resultVO.setMsg(e.getMessage());

        } else if (e instanceof HttpRequestMethodNotSupportedException) {

            resultVO.setMsg("请求错误");

            log.warn("非法请求:{},url:{}", e.getMessage(), request.getRequestURI());
        } else {

            resultVO.setMsg("系统繁忙,请稍后重试");

            log.error("Controller统一异常处理, 路径:" + request.getRequestURI(), e);
        }

        resultVO.setCode(ResultConstant.RESULT_CODE_500);


        return resultVO;
    }


}
