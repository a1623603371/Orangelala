package com.mayikt.member.service;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.output.dto.UserOutDTO;
import com.mayikt.weixin.input.dto.AppInpDTO;
import com.mayikt.bese.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * 会员服务接口
 */
@Api(tags = "会员服务接口")
public interface MemberService {
    /**
     *会员服务调用微信服务
     * @return
     */
    @ApiOperation(value = "会员服务调用微信服务")
    @GetMapping("/memberToWeiXin")
    public BaseResponse<AppInpDTO> memberToWeiXin();

    /**
     * 查询手机号码是否存在
     * @param mobile
     * @return
     */
    @ApiOperation(value = "根据手机号码查询是否已经存在")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", required = true, value = "用户手机号码"), })
    @PostMapping("/existMobile")
    BaseResponse<UserOutDTO> existMobile(@RequestParam("mobile") String mobile);
    /**
     * 根据token查询用户信息
     *
     * @param token
     * @return
     */
    @GetMapping("/getUserInfo")
    @ApiOperation(value = "根据token查询用户信息")
    BaseResponse<UserOutDTO> getInfo(@RequestParam("token") String token);

    /**
     * SSO认证系统登陆接口
     *
     * @param userLoginInpDTO
     * @return
     */

    @PostMapping("/ssoLogin")
    @ApiOperation(value = "sso认证登录" )
    public BaseResponse<UserOutDTO> ssoLogin(@RequestBody UserLoginInpDTO userLoginInpDTO);

}
