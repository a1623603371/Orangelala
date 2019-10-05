package com.mayikt.core.error;

import com.alibaba.fastjson.JSONObject;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
/**
* @Description:    全局捕获异常
* @Author:         yc
* @CreateDate:     2019/6/26 12:17
* @UpdateUser:     yc
* @UpdateDate:     2019/6/26 12:17
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends BaseApiService<JSONObject> {
    @ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public BaseResponse<JSONObject> exceptionHandler(Exception e) {
		log.info("###全局捕获异常###,error:{}", e);
		return setResultError("系统错误!");
	}
}
