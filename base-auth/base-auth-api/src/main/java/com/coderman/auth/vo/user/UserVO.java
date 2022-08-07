package com.coderman.auth.vo.user;

import com.coderman.auth.model.user.UserModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author coderman
 * @Title: 用户vo
 * @Description: TOD
 * @date 2022/2/2711:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户VO")
public class UserVO extends UserModel {

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "角色信息")
    private List<String> roleList;
}
