package com.mayikt.member.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.mayikt.core.bean.MiteBeanUtils;
import com.mayikt.core.utils.MD5Util;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.core.type.TypeCastHelper;
import com.mayikt.member.input.dto.UserLoginInpDTO;
import com.mayikt.member.mapper.entity.UserDO;
import com.mayikt.member.mapper.UserMapper;
import com.mayikt.member.feign.WeiXinServiceFeign;
import com.mayikt.member.output.dto.UserOutDTO;
import com.mayikt.member.service.MemberService;
import com.mayikt.weixin.input.dto.AppInpDTO;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.constants.Constants;
import com.xxl.sso.core.store.SsoLoginStore;
import com.xxl.sso.core.user.XxlSsoUser;
import com.xxl.sso.core.util.JedisUtil;
import org.apache.commons.codec.digest.XXHash32;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
/**
* @Description:    会员服务实现类
* @Author:         yc
* @CreateDate:     2019/6/16 16:05
* @UpdateUser:     yc
* @UpdateDate:     2019/6/16 16:05
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@RestController
public class MemberServiceImpl extends BaseApiService<UserOutDTO> implements MemberService {
    @Autowired
    private WeiXinServiceFeign weiXinServiceFeign;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public BaseResponse<AppInpDTO> memberToWeiXin() {
        return weiXinServiceFeign.getApp();
    }

    /**
     * 查询手机号码是否存在
     *
     * @param mobile
     * @return
     */
    @Override
    public BaseResponse<UserOutDTO> existMobile(String mobile) {
        //检查手机是否为空
        if (StringUtils.isEmpty(mobile)){
            return  setResultError("手机号不能为空");

        }
        UserDO userDO=userMapper.existMobile(mobile);
        if (userDO==null){
            return setResultError(Constants.HTTP_RES_CODE_203,"用户不存在");
        }
        return setResultSuccess( MiteBeanUtils.doToDto(userDO,UserOutDTO.class));
    }

    /**
     * 根据token查询用户信息
     *
     * @param token
     * @return
     */
    @Override
    public BaseResponse<UserOutDTO> getInfo(String token) {
        //验证token
        if (StringUtils.isEmpty(token)){
            return setResultError("令牌已失效或不存在");
        }

        //使用token查询redis
   String xxlSsoUserJSON =redisUtil.getString(token);
        XxlSsoUser xxlSsoUser=JSONObject.parseObject(xxlSsoUserJSON,XxlSsoUser.class);

        Long userid= Long.valueOf(xxlSsoUser.getUserid());
        //使用用户id查询用户信息
        UserDO userDO=userMapper.findByUserId(userid);
        if (userDO==null){
            return setResultError("用户不存在");
        }
        return setResultSuccess(MiteBeanUtils.doToDto(userDO,UserOutDTO.class));
    }

    /**
     * SSO认证系统登陆接口
     *
     * @param userLoginInpDTO
     * @return
     */
    @Override
    public BaseResponse<UserOutDTO> ssoLogin(@RequestBody UserLoginInpDTO userLoginInpDTO) {
        // 1.验证参数
        String mobile = userLoginInpDTO.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            return setResultError("手机号码不能为空!");
        }
        String password = userLoginInpDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 判断登陆类型
        String loginType = userLoginInpDTO.getLoginType();
        if (StringUtils.isEmpty(loginType)) {
            return setResultError("登陆类型不能为空!");
        }
        // 目的是限制范围
        if (!(loginType.equals(Constants.MEMBER_LOGIN_TYPE_ANDROID) || loginType.equals(Constants.MEMBER_LOGIN_TYPE_IOS)
                || loginType.equals(Constants.MEMBER_LOGIN_TYPE_PC))) {
            return setResultError("登陆类型出现错误!");
        }

        // 设备信息
        String deviceInfor = userLoginInpDTO.getDeviceInfor();
        if (StringUtils.isEmpty(deviceInfor)) {
            return setResultError("设备信息不能为空!");
        }
        // 2.对登陆密码实现加密
        String newPassWord = MD5Util.MD5(password);
        // 3.使用手机号码+密码查询数据库 ，判断用户是否存在
        UserDO userDo = userMapper.login(mobile, newPassWord);
        if (userDo == null) {
            return setResultError("用户名称或者密码错误!");
        }
        return setResultSuccess(MiteBeanUtils.doToDto(userDo, UserOutDTO.class));


}
}
