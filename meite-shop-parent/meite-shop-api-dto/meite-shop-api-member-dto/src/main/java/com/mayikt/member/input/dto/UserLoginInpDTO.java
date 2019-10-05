package com.mayikt.member.input.dto;

import java.util.Date;

import com.mayikt.member.output.dto.UserOutDTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * 
 * 
 * @description: 登陆请求参数
 * @author: 97后互联网架构师-余胜军
 * @contact: QQ644064779、微信yushengjun644 www.mayikt.com
 * @date: 2019年1月3日 下午3:03:17
 * @version V1.0
 * @Copyright 该项目“基于SpringCloud2.x构建微服务电商项目”由每特教育|蚂蚁课堂版权所有，未经过允许的情况下，
 *            私自分享视频和源码属于违法行为。
 */
@Data
@ApiModel(value = "用户登陆请求参数")
public class UserLoginInpDTO {
	/**
	 * 手机号码
	 */
	@ApiModelProperty(value = "手机号码")
	private String mobile;

	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

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
    /**
     * qq联合登入
     */
    @ApiModelProperty(value = "qqoenid")
    private  String qqOpenId;
    /**
     * 微信联合登入
     */
    @ApiModelProperty("微信的opedid")
    private String wxOpenId;

}
