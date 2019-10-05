package com.mayikt.weixin.service.impl;


import com.mayikt.weixin.input.dto.AppInpDTO;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayitk.entity.AppEntity;

import com.mayikt.weixin.service.WeiXinService;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信接口实现
 */
@RestController
public class WenXinServiceIml extends BaseApiService implements WeiXinService {
    /**
     * 获取应用接口
     */

    @Override
    public BaseResponse<AppInpDTO> getApp() {
        return setResultSuccess(new AppEntity("1231","小号"));
    }
}
