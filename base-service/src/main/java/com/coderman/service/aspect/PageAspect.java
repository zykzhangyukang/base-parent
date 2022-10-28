package com.coderman.service.aspect;


import com.coderman.api.constant.AopConstant;
import com.coderman.api.constant.ResultConstant;
import com.coderman.api.vo.PageVO;
import com.coderman.api.vo.ResultVO;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@Aspect
@Order(value = AopConstant.PAGE_ASPECT_ORDER)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PageAspect {

    @SuppressWarnings("all")
    @Around("@annotation(com.coderman.service.anntation.PageLimit) && @annotation(com.coderman.swagger.annotation.ApiReturnParams) && (execution(* com.coderman..controller..*(..)) || execution(* com.coderman..api..*(..)))")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Object result = point.proceed();

        if (result instanceof ResultVO) {

            String maxQueryCount = "20000";
            Integer pageRow = 20;


            ResultVO resultVO = (ResultVO) result;

            if (StringUtils.isNotBlank(maxQueryCount) && null != resultVO.getResult()) {

                boolean flag = false;

                if (resultVO.getResult() instanceof PageVO && (((PageVO<?>) resultVO.getResult()).getTotalRow() > Long.parseLong(maxQueryCount))) {


                    PageVO pageVO = (PageVO) resultVO.getResult();

                    pageRow = pageVO.getPageRow();
                    pageVO.setTotalPage(((Integer.parseInt(maxQueryCount) - 1) / pageVO.getPageRow()) + 1);


                    if (pageVO.getCurrPage() > pageVO.getTotalPage()) {
                        pageVO.setDataList(null);
                        flag = true;
                    } else if (pageVO.getCurrPage() == pageVO.getTotalPage()) {

                        int endSize = Integer.parseInt(maxQueryCount) % pageVO.getPageRow();
                        List list = new ArrayList<>((Collection<?>) pageVO.getDataList());
                        pageVO.setDataList(list.subList(0, endSize - 1));
                    }
                }


                if (resultVO.getResult() instanceof Collection && (((Collection<?>) resultVO.getResult()).size() > Integer.parseInt(maxQueryCount))) {

                    List list = new ArrayList<>((Collection<?>) resultVO.getResult());
                    resultVO.setResult(list.subList(0, Integer.parseInt(maxQueryCount) - 1));
                    flag = true;
                }


                if (flag) {

                    resultVO.setCode(ResultConstant.RESULT_CODE_402);
                    resultVO.setMsg("系统限制只能显示前" + maxQueryCount + "条记录（最大只能跳转到" + (((Integer.parseInt(maxQueryCount) - 1) / pageRow) + 1) + "页),请把筛选缩小");
                }
            }
        }

        return result;
    }
}
