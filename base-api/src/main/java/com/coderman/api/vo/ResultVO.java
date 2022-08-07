package com.coderman.api.vo;

import com.coderman.api.constant.ResultConstant;
import com.coderman.api.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author coderman
 * @Title: 公共返回对象
 * @Description: TOD
 * @date 2022/1/115:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> extends BaseModel {

    /**
     * 状态码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;


    /**
     * 响应结果
     */
    private T result;


    /**
     * 响应成功
     *
     * @return
     */
    public boolean isSuccess() {
        return ResultConstant.RESULT_CODE_200.equals(this.code);
    }

    /**
     * 响应失败
     *
     * @return
     */
    public boolean isFail() {
        return !ResultConstant.RESULT_CODE_200.equals(code);
    }
}
