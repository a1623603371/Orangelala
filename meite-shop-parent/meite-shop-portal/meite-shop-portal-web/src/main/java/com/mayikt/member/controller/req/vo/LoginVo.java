package com.mayikt.member.controller.req.vo;

import lombok.Data;

@Data
public class LoginVo {

	/**
	 * 手机号码
	 */
	private String mobile;
	/**
	 * 手机密码
	 */
	private String password;
	/**
	 * 图形验证码
	 */
	private String graphicCode;
    /**
     * qq联合登入
     */
  //  private  String qqOpenid;
}
