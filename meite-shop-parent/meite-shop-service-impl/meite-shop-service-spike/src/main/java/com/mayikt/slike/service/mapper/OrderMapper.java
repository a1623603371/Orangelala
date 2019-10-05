package com.mayikt.slike.service.mapper;

import com.mayikt.slike.service.mapper.entity.OrderEntity;
import org.apache.ibatis.annotations.Insert;


public interface OrderMapper {

	@Insert("INSERT INTO `meite_order` VALUES (#{seckillId},#{userPhone}, '1', now());")
	int insertOrder(OrderEntity orderEntity);
}
