package com.mayikt.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @Description:    微信服务注册码验证接口
* @Author:         yc
* @CreateDate:     2019/6/21 17:11
* @UpdateUser:     yc
* @UpdateDate:     2019/6/21 17:11
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Api(tags = "微信注册码验证码接口1")
public interface VerificaCodeService {

	/**
     * 功能说明:根据手机号码验证码token是否正确
	 * 
	 * @return
	 */
	@ApiOperation(value = "根据手机号码验证码token是否正确1")
	@GetMapping("/verificaWeixinCode")
	@ApiImplicitParams({
			// @ApiImplicitParam(paramType="header",name="name",dataType="String",required=true,value="用户的姓名",defaultValue="zhaojigang"),
			@ApiImplicitParam(paramType = "query", name = "phone", dataType = "String", required = true, value = "用户手机号码"),
			@ApiImplicitParam(paramType = "query", name = "weixinCode", dataType = "String", required = true, value = "微信注册码") })
	public BaseResponse<JSONObject> verificaWeixinCode(@RequestParam("phone") String phone,@RequestParam("weixinCode") String weixinCode);
}
