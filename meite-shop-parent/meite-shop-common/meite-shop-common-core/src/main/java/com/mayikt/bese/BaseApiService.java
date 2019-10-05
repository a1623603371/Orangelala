package com.mayikt.bese;

import com.mayikt.constants.Constants;
import com.mayikt.core.bean.MiteBeanUtils;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
* @Description:    微服务接口实现该接口可以使用传提参数可以直接封装统一返回结果集
* @Author:         yc
* @CreateDate:     2019/6/21 13:25
* @UpdateUser:     yc
* @UpdateDate:     2019/6/21 13:25
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
@Component
public class BaseApiService<T> {

    private Class clazz;

	public BaseResponse<T> setResultError(Integer code, String msg) {
        return setResult(code, msg, null);
	}

	// 返回错误，可以传msg
	public BaseResponse<T> setResultError(String msg) {
		return setResult(Constants.HTTP_RES_CODE_500, msg, null);
	}

	// 返回成功，可以传data值
	public BaseResponse<T> setResultSuccess(T data) {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, data);
	}

	// 返回成功，沒有data值
	public BaseResponse<T> setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200, Constants.HTTP_RES_CODE_200_VALUE, null);
	}

	// 返回成功，沒有data值
	public BaseResponse<T> setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200, msg, null);
	}

	// 通用封装
	public BaseResponse<T> setResult(Integer code, String msg, T data) {
		return new BaseResponse<T>(code, msg, data);
	}

    // 调用数据库层判断
    public Boolean toDaoResult(int result) {
        return result > 0 ? true : false;
    }
    //DO转DTO
    public <D> BaseResponse<T> setResultSuccess1(D d){
        //这里this代表的是BaseDaoImpl的子类对象
        //得到当当前类（父类）上的泛型T--父类型
        Type type = this.getClass().getGenericSuperclass();
        //得到当前类上所有的泛型类型Class
        Type[] types = ((ParameterizedType) type).getActualTypeArguments();
        //得到具体传入的泛型类Class对象
        clazz = (Class) types[0];
        System.out.println(clazz);
        return setResultSuccess((T) MiteBeanUtils.doToDto(d,clazz));
    }
    // 接口直接返回true 或者false
    public Boolean isSuccess(BaseResponse<?> baseResp) {
        if (baseResp == null) {
            return false;
        }
        if (baseResp.getCode().equals(Constants.HTTP_RES_CODE_500)) {
            return false;
        }
        return true;
    }
}