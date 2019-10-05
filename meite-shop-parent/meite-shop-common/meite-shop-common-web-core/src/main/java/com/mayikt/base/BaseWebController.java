package com.mayikt.base;

import javax.servlet.http.HttpServletRequest;

import com.mayikt.bese.BaseResponse;
import com.mayikt.constants.Constants;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.UserAgent;
import nl.bitwalker.useragentutils.Version;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;



public class BaseWebController {
	/**
	 * 500页面
	 */
	protected static final String ERROR_500_FTL = "500";

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

	/**
	 * 获取浏览器信息
	 * 
	 * @return
	 */
	public String webBrowserInfo(HttpServletRequest request) {
		// 获取浏览器信息
		Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
		// 获取浏览器版本号
		Version version = browser.getVersion(request.getHeader("User-Agent"));
		String info = browser.getName() + "/" + version.getVersion();
		return info;
	}

	public void setErrorMsg(Model model, String errorMsg) {
		model.addAttribute("error", errorMsg);
	}

}
