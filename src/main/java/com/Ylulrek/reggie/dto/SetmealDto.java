package com.Ylulrek.reggie.dto;


import com.Ylulrek.reggie.domain.Setmeal;
import com.Ylulrek.reggie.domain.SetmealDish;
import lombok.Data;

import java.util.List;


@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
