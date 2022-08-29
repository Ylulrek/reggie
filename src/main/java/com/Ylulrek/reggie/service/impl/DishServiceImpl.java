package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.common.CustomException;
import com.Ylulrek.reggie.domain.Dish;
import com.Ylulrek.reggie.domain.DishFlavor;
import com.Ylulrek.reggie.dto.DishDto;
import com.Ylulrek.reggie.mapper.DishMapper;
import com.Ylulrek.reggie.service.DishFlavorService;
import com.Ylulrek.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors=flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        /*for (DishFlavor flavor : flavors){
            flavor.setDishId(dishId);
        }*/

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        DishDto dishDto=new DishDto();
        //查询菜品基本信息
        Dish dish = this.getById(id);
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品对应口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor :: getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        boolean update = this.updateById(dishDto);
        //清理当前菜品的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor :: getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加提交过来的口味数据
        List<DishFlavor> flavors=dishDto.getFlavors();

        flavors=flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void deleteByIdsWithFlavor(Long[] ids) {
        for (Long id:ids){
            Dish dish = this.getById(id);
            if (dish.getStatus()!=1) {
                this.removeById(id);
                //清理当前菜品的口味数据
                LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DishFlavor::getDishId, id);
                dishFlavorService.remove(queryWrapper);
            }else {
                throw new CustomException("菜品正在售卖中，不能删除");
            }
        }
    }

    @Override
    public void updateStatus(Integer status, Long[] ids) {
        for (Long id:ids){
            Dish dish = this.getById(id);
            dish.setStatus(status);
            this.updateById(dish);
        }
    }
}
