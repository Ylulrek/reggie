package com.Ylulrek.reggie.dto;


import com.Ylulrek.reggie.domain.Dish;
import com.Ylulrek.reggie.domain.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
