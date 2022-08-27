package com.coderman.service.dict;

import com.coderman.api.model.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author coderman
 * @Title: 常量项
 * @Description: TOD
 * @date 2022/1/2519:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ConstItem extends BaseModel {

    private Object code;

    private String name;


    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ConstItem(Object code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ConstantItem{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}'+'\n';
    }
}
