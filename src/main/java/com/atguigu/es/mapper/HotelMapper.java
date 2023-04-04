package com.atguigu.es.mapper;

import com.atguigu.es.pojo.Hotel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HotelMapper {
    @Select("select * from tb_hotel")
    List<Hotel> list();
}
