package com.mayikt.pay.service;

import com.mayikt.weixin.out.dto.PaymentChannelDTO;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;


/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.service
 * @ClassName: PaymentChannelService
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 12:32
 * @Version: 1.0
 */
public interface PaymentChannelService {
    /**
     * 查询所有支付渠道
     *
     */
    @GetMapping("/selectAll")
    public List<PaymentChannelDTO> selectAll();
}
