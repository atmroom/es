package com.atguigu.es.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class HotelDoc {
    private Long id;
    private String name;
    private String address;
    private Integer price;
    private Integer score;
    private String brand;
    private String city;
    private String star_name;
    private String business;
    private String location;
    private String pic;
    private List<String> suggestion;
    public HotelDoc(Hotel hotel){
        id = hotel.getId();
        name = hotel.getName();
        address = hotel.getAddress();
        price = hotel.getPrice();
        score = hotel.getScore();
        brand = hotel.getBrand();
        city = hotel.getCity();
        star_name = hotel.getStar_name();
        business = hotel.getBusiness();
        location = hotel.getLatitude() + ", "+hotel.getLongitude();
        pic = hotel.getPic();
        if (business.contains("/")){
            String[] split = business.split("/");

            suggestion = new ArrayList<>();
            suggestion.addAll(Arrays.asList(split));
            suggestion.add(brand);
        }else {
            suggestion = Arrays.asList(brand,business);

        }
    }
}
