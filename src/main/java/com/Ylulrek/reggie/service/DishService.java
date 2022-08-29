package com.Ylulrek.reggie.service;

import com.Ylulrek.reggie.domain.Dish;
import com.Ylulrek.reggie.dto.DishDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    //新增菜品，以及其口味数据
    public void saveWithFlavor(DishDto dishDto);
    
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);

    public void deleteByIdsWithFlavor(Long[] ids);

    public void updateStatus(Integer status, Long[] ids);
}
