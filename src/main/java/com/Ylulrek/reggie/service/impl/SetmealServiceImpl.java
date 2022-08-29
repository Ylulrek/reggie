package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.common.CustomException;
import com.Ylulrek.reggie.domain.Dish;
import com.Ylulrek.reggie.domain.DishFlavor;
import com.Ylulrek.reggie.domain.Setmeal;
import com.Ylulrek.reggie.domain.SetmealDish;
import com.Ylulrek.reggie.dto.SetmealDto;
import com.Ylulrek.reggie.mapper.SetmealMapper;
import com.Ylulrek.reggie.service.SetmealDishService;
import com.Ylulrek.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Resource
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        //保存套餐和菜品的关联信息
        setmealDishService.saveBatch(setmealDishes);

    }

    @Override
    @Transactional
    public void deleteByIdsWithDish(Long[] ids) {
        for (Long id:ids){
            log.info("id="+id);
            Setmeal setmeal = this.getById(id);
            if (setmeal.getStatus()!=1) {
                this.removeById(id);
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId, id);
                setmealDishService.remove(queryWrapper);
            }else {
                throw new CustomException("套餐正在售卖中，不能删除");
            }
        }
    }

    @Override
    public void updateStatus(Integer status, Long[] ids) {
        for (Long id:ids){
            Setmeal setmeal = this.getById(id);
            setmeal.setStatus(status);
            this.updateById(setmeal);
        }
    }
}
