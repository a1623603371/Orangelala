package com.mayikt.member.service;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.input.dto.UserExitDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.member.service
 * @ClassName: MemberExitService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/29 10:42
 * @Version: 1.0
 */
@Api(tags = "退出服务接口")
public interface MemberExitService {
    /**
     * 用户退出接口
     *
     * @param userExitDTO
     * @return
     */
    @PostMapping("/exit")
    @ApiOperation(value = "会员用户退出接口")
    BaseResponse<JSONObject> exit(@RequestBody UserExitDTO userExitDTO);


}
