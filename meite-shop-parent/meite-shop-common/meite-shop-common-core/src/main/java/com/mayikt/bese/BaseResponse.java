package com.mayikt.bese;

import lombok.Data;
/**
* @Description:   微服务接口统一返回码
* @Author:         yc
* @CreateDate:     2019/6/21 13:22
* @UpdateUser:     yc
* @UpdateDate:     2019/6/21 13:22
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
public class BaseResponse<T> {
    /**
     * 返回码
     */
	private Integer code;
	/*
	 *返回消息
	 */
    private String msg;
    /**
     * 返回数据
     */
	private T data;

	public BaseResponse() {

	}

	public BaseResponse(Integer code, String msg, T data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

}
