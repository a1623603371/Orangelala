package com.mayikt.member.input.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.input.dto
 * @ClassName: UserExitDto
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/29 10:51
 * @Version: 1.0
 */
@Api(tags = "退出请求参数")
@Data
public class UserExitDTO {
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;

    /**
     * 登陆类型 PC端 移动端 安卓 IOS 平板
     */
    @ApiModelProperty(value = "登陆类型")
    private String loginType;
    /**
     * 登入的设备
     */
    @ApiModelProperty(value = "设备信息")
    private  String deviceInfor;


}
