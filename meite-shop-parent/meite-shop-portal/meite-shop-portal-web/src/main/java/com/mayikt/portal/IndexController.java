package com.mayikt.portal;

import com.mayikt.base.BaseWebController;
import com.mayikt.bese.BaseResponse;
import com.mayikt.member.feign.MemberServiceFeign;
import com.mayikt.member.output.dto.UserOutDTO;
import com.mayikt.member.service.MemberLoginService;
import com.mayikt.web.constants.Constants;
import com.mayikt.web.utils.CookieUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @Description:    首页
* @Author:         yc
* @CreateDate:     2019/6/28 15:19
* @UpdateUser:     yc
* @UpdateDate:     2019/6/28 15:19
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Controller
public class IndexController extends BaseWebController {
    @Autowired
    private MemberServiceFeign memberServiceFeign;
    /**
     * 跳转到index页面
     */
    private static final String INDEX_FTL = "index";
    /**
     * 跳转到登录
     */
    private static final String MB_LOGIN_FTL = "member/login";

    @RequestMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 1.从cookie 中 获取 会员token//Constants.LOGIN_TOKEN_COOKIENAME
        String token = CookieUtils.getCookieValue(request,"xxl_sso_sessionid" , true);
        System.out.println(token+"***WDE***");
        if (!StringUtils.isEmpty(token)) {
            // 2.调用会员服务接口,查询会员用户信息
            BaseResponse<UserOutDTO> userInfo = memberServiceFeign.getInfo(token);
            if (isSuccess(userInfo)) {
                UserOutDTO data = userInfo.getData();
                if (data != null) {
                    String mobile = data.getMobile();
                    // 对手机号码实现脱敏
                    String desensMobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    model.addAttribute("desensMobile", desensMobile);
                }

            }
            return INDEX_FTL;
        }
        return MB_LOGIN_FTL;
    }
}
