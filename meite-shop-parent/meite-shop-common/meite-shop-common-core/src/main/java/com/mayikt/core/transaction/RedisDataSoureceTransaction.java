package com.mayikt.core.transaction;

import com.mayikt.core.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * @ProjectName: redis+数据库事务
 * @Package: com.mayikt.core.transaction
 * @ClassName: RedisDataSoureceTransaction
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/6/27 21:34
 * @Version: 1.0
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RedisDataSoureceTransaction {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 事务管理器
     */
    @Autowired
    private DataSourceTransactionManager dataSourceTransactionManager;

    public TransactionStatus begin() {
        //手动开启事务
        //1.开启数据库，开启事务传播行为级别
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(new DefaultTransactionAttribute());
        //开启redis事务
        redisUtil.begin();
        return transactionStatus;
    }

    /**
     * 提交事务
     */
    public void commit(TransactionStatus transactionStatus) throws Exception {
        if (transactionStatus == null) {
            throw new Exception("transactionStatus is no");
        }
        //提交数据库事务和redis事务，redis会和数据库一起提交
        dataSourceTransactionManager.commit(transactionStatus);

    }

    /***
     * 回滚事务
     */
    public void rollback(TransactionStatus transactionStatus) throws Exception {
        if (transactionStatus == null) {
            throw new Exception("transactionStatus is no");
        }
        //回滚数据库和redis事务
        dataSourceTransactionManager.rollback(transactionStatus);
        //回滚redis事务
        //redisUtil.discard();
    }

}