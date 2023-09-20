package com.coderman.service.aop;

import com.coderman.api.constant.CommonConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.exception.BusinessException;
import com.coderman.api.exception.RateLimitException;
import com.coderman.api.vo.ResultVO;
import com.coderman.service.service.BaseService;
import com.coderman.service.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author coderman
 * @Title: 全局异常处理
 * @date 2022/3/519:01
 */
@ControllerAdvice
public class GlobalExceptionHandler extends BaseService {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ResultVO<Void>> handlerException(HttpServletRequest request, Exception e) {

        ResultVO<Void> resultVO = new ResultVO<>();

        if (e instanceof BusinessException) {

            resultVO.setMsg(e.getMessage());
            resultVO.setCode(ResultConstant.RESULT_CODE_402);

        } else if (e instanceof RateLimitException) {

            resultVO.setMsg("请求过于频繁！");
            resultVO.setCode(ResultConstant.RESULT_CODE_429);

            log.warn("请求过于频繁,ip:{} , url:{}", IpUtil.getIpAddr(), request.getRequestURI());


        } else if (e instanceof HttpRequestMethodNotSupportedException) {

            resultVO.setMsg("请求错误！");
            resultVO.setCode(ResultConstant.RESULT_CODE_400);

            log.warn("非法请求:{},url:{}", e.getMessage(), request.getRequestURI());

        }else if(e instanceof NoHandlerFoundException){

            resultVO.setMsg("接口不存在！");
            resultVO.setCode(ResultConstant.RESULT_CODE_404);

            log.warn("接口不存在:{},url:{}", e.getMessage(), request.getRequestURI());

        } else {

            resultVO.setCode(ResultConstant.RESULT_CODE_500);
            resultVO.setMsg("系统繁忙,请稍后重试！");

            log.error(CommonConstant.GLOBAL_FAIL_MSG + ":{}", request.getRequestURI(), e);
        }

        return new ResponseEntity<>(resultVO , HttpStatus.valueOf(resultVO.getCode()));
    }


}
