package com.mayikt.pay.job.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.pay.job.job.handler
 * @ClassName: PayJobHandler
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/24 14:33
 * @Version: 1.0
 */
@JobHandler("payJobHandler")
@Component
@Slf4j
public class PayJobHandler extends IJobHandler {

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(">>>>使用任务调度实现自动化对账");
        return SUCCESS;
    }
}
