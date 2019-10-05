package com.mayikt.twitter;

/**
 * @ProjectName: 雪花算法生成全局id
 * @Package: com.mayikt.twitter
 * @ClassName: SonwflakeldUtils
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 18:02
 * @Version: 1.0
 */
public class SonwflakeldUtils {
private static   SnowflakeIdWorker idWorker;
static {
    //使用静态代码块初始化SnowflakeIdWorker
    idWorker =new SnowflakeIdWorker(1,1);
}
public static String nextId(){
    return idWorker.nextId()+"";
}
}

