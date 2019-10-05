package com.mayikt.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;

/**
 * @ProjectName: Orangelala
 * @Package: com.mayikt.mapper
 * @ClassName: MapperUtils
 * @Author: Administrator
 * @Description: ${description}
 * @Date: 2019/8/9 15:19
 * @Version: 1.0
 */
public class MapperUtils {
    static MapperFactory mapperFactory;
    static{
        mapperFactory=new DefaultMapperFactory.Builder().build();
    }
    public static <S,D>List<D>mapAsList(Iterable<S> source,Class<D> destinationClass ){
        return mapperFactory.getMapperFacade().mapAsList(source,destinationClass);
    }
}
