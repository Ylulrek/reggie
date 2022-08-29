package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.common.CustomException;
import com.Ylulrek.reggie.domain.Category;
import com.Ylulrek.reggie.domain.Dish;
import com.Ylulrek.reggie.domain.Setmeal;
import com.Ylulrek.reggie.mapper.CategoryMapper;
import com.Ylulrek.reggie.service.CategoryService;
import com.Ylulrek.reggie.service.DishService;
import com.Ylulrek.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    //根据id删除分类
    @Override
    public void remove(Long id) {
        //添加查询条件
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<Dish>();
        dishLambdaQueryWrapper.eq(Dish :: getCategoryId,id);
        //查询分类是否关联了菜品
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1>0){
            //关联了菜品，抛出异常
            throw new CustomException("当前分类项关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<Setmeal>();
        setmealLambdaQueryWrapper.eq(Setmeal :: getCategoryId,id);
        //查询分类是否关联了套餐
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2>0){
            //关联了套餐，抛出异常
            throw new CustomException("当前分类项关联了套餐，不能删除");
        }
        //进行删除操作
        super.removeById(id);
    }
}
