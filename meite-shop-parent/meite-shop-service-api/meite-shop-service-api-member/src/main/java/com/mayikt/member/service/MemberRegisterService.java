package com.mayikt.member.service;

import com.mayikt.member.input.dto.UserInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api(tags = "会员注册")
public interface MemberRegisterService {
    /**
	 * 用户注册接口
	 * 
	 * @param userInpDTO
	 * @return
	 */
    @ApiOperation(value = "会员用户注册信息接口2")
	@PostMapping("/register")
    BaseResponse<JSONObject> register(@RequestBody UserInpDTO userInpDTO,
                                      @RequestParam("registCode") String registCode);

}
