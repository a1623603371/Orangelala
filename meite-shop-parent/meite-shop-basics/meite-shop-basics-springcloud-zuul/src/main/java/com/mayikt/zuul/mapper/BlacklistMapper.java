package com.mayikt.zuul.mapper;


import com.mayikt.zuul.mapper.entity.MeiteBlacklist;
import org.apache.ibatis.annotations.Select;

public interface BlacklistMapper {

	@Select(" select ID AS ID ,ip_addres AS ipAddres,restriction_type  as restrictionType, availability  as availability from meite_blacklist where  ip_addres =#{ipAddres} and  restriction_type='1' ")
    MeiteBlacklist findBlacklist(String ipAddres);

}
