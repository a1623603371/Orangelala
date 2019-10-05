package com.mayikt.weixin.service;

import com.mayikt.weixin.input.dto.AppInpDTO;
import com.mayikt.bese.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ProjectName: 微信服务接口
 * @Package: com.mayitk.weixin.service
 * @ClassName: WeixinService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/16 22:47
 * @Version: 1.0
 */
@Api(tags = "微信服务接口2")
public interface WeiXinService {
    /**
    *  微信获取应用
    * @author      作者姓名
    * @param
    * @return
    * @exception
    * @date        2019/6/16 22:49
    */
      @GetMapping("/getApp")
      @ApiOperation("获取应用2")
      public BaseResponse<AppInpDTO> getApp();
}
