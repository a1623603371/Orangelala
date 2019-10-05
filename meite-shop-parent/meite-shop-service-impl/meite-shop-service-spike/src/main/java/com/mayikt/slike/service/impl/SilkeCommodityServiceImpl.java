package com.mayikt.slike.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.mayikt.api.spike.service.SpikeCommodityService;
import com.mayikt.bese.BaseApiService;
import com.mayikt.bese.BaseResponse;
import com.mayikt.bese.GenerateToken;
import com.mayikt.core.utils.RedisUtil;
import com.mayikt.slike.producer.SpikeCommodityProducer;
import com.mayikt.slike.service.mapper.OrderMapper;
import com.mayikt.slike.service.mapper.SeckillMapper;
import com.mayikt.slike.service.mapper.entity.OrderEntity;
import com.mayikt.slike.service.mapper.entity.SeckillEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.TimeUnit;
/**
 * @ProjectName: 秒杀服务
 * @Package: com.mayikt.slike.service.impl
 * @ClassName: SilkeCommodityServiceImpl
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/9/3 20:29
 * @Version: 1.0
 */
@RestController
@Slf4j
public class SilkeCommodityServiceImpl extends BaseApiService<JSONObject> implements SpikeCommodityService {
    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private GenerateToken generateToken;
    @Autowired
    private SpikeCommodityProducer spikeCommodityProducer;
    /**
     * 用户秒杀接口 phone和userid都可以的
     *
     * @param phone
     * @param seckillId
     * @return
     * @phone 手机号码<br>
     * @seckillId 库存id
     */
    @Override
    @Transactional
    public BaseResponse<JSONObject> spike(String phone, Long seckillId) {
        //1.验证参数
        if (StringUtils.isEmpty(phone)){
        return setResultError("手机号不能为空");
        }
        if (seckillId==null){
            return setResultError("订单号为空");
        }
        //从redis中获取对应的秒杀token
        String seckillToken=generateToken.getListKeyToken(seckillId+"");
        if (StringUtils.isEmpty(seckillToken)) {
            log.info(">>>seckillId:{}, 亲，该秒杀已经售空，请下次再来!", seckillId);
            return setResultError("亲，该秒杀已经售空，请下次再来!");
        }
        //获取秒杀token后，异步放入mq中实现修改商品库存
        sendSeckillMsg(seckillId,phone);
        return setResultSuccess("正在排队中.......");



     /*   //查询商品信息
        SeckillEntity seckillEntity=seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity==null){
            return  setResultError("商品信息不存在");
        }
        //2.用户频率限制
        Boolean resultNx=redisUtil.setNx(phone,seckillId+"", 10L);
        //3.修改数据库库存，1万中只有100个抢购成功 提前生成好100个token 谁能够抢购成功token放入到mq中实现异步修改库存
        Long version=seckillEntity.getVersion();
        int inventoryDeduction=seckillMapper.inventoryDeduction(seckillId,version);
        if (!toDaoResult(inventoryDeduction)) {
            log.info(">>>修改库存失败>>>>inventoryDeduction返回为{} 秒杀失败！", inventoryDeduction);
            return setResultError("亲，请稍后重试");
        }
        //添加秒杀订单 基于mq形式异步
        OrderEntity orderEntity=new OrderEntity();
        orderEntity.setSeckillId(seckillId);
        orderEntity.setUserPhone(phone);
        int insertOrder=orderMapper.insertOrder(orderEntity);
        if (!toDaoResult(insertOrder)){
            log.info("亲，请稍后重试!");
        }
        log.info(">>>修改库存成功>>>>inventoryDeduction返回为{} 秒杀成功", inventoryDeduction);
        return setResultSuccess("恭喜您，秒杀成功");*/
    }
    /**
     * 获取到秒杀token之后，异步放入mq中实现修改商品的库存
     */
    @Async
    private void sendSeckillMsg(Long seckillId, String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("seckillId", seckillId);
        jsonObject.put("phone", phone);
        spikeCommodityProducer.send(jsonObject);
    }

    /**
     * is数据库类型为 list类型 key为 商品库存id list 多个秒杀token
     *
     * @param seckillId
     * @param tokenQuantity
     * @return
     */
    @Override
    public BaseResponse<JSONObject> addSpikeToken(Long seckillId, Long tokenQuantity) {
        // 1.验证参数
        if (seckillId == null) {
            return setResultError("商品库存id不能为空!");
        }
        if (tokenQuantity == null) {
            return setResultError("token数量不能为空!");
        }
        SeckillEntity seckillEntity = seckillMapper.findBySeckillId(seckillId);
        if (seckillEntity == null) {
            return setResultError("商品信息不存在!");
        }
        // 2.使用多线程异步生产令牌
        createSeckillToken(seckillId, tokenQuantity);
        return setResultSuccess("令牌正在生成中.....");
    }

    /**
     * 生成商品Token
     * @param seckillId
     * @param tokenQuantity
     */
    @Async
    private void createSeckillToken(Long seckillId, Long tokenQuantity) {
        generateToken.createListToken("seckill_", seckillId + "", tokenQuantity);
    }

}
