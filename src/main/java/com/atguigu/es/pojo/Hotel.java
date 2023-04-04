package com.atguigu.es.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Hotel {
    private Long id;
    private String name;
    private String address;
    private Integer price;
    private Integer score;
    private String brand;
    private String city;
    private String star_name;
    private String business;
    private String latitude;
    private String longitude;
    private String pic;
}
