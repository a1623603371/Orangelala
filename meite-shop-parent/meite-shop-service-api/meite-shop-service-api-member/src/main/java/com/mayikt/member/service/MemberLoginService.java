package com.mayikt.member.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @Description:    用户登入接口
* @Author:         yc
* @CreateDate:     2019/6/26 20:31
* @UpdateUser:     yc
* @UpdateDate:     2019/6/26 20:31
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Api(tags = "用户登陆服务接口")
public interface MemberLoginService {
    /**
	 * 用户登陆接口
	 * 
	 * @param userLoginInpDTO
	 * @return
	 */
	@PostMapping("/login")
	@ApiOperation(value = "会员用户登陆信息接口")
    BaseResponse<JSONObject> login(@RequestBody UserLoginInpDTO userLoginInpDTO);

}
