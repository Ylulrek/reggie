package com.Ylulrek.reggie.controller;

import com.Ylulrek.reggie.common.R;
import com.Ylulrek.reggie.domain.Category;
import com.Ylulrek.reggie.domain.Dish;
import com.Ylulrek.reggie.domain.DishFlavor;
import com.Ylulrek.reggie.dto.DishDto;
import com.Ylulrek.reggie.service.CategoryService;
import com.Ylulrek.reggie.service.DishFlavorService;
import com.Ylulrek.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private DishService dishService;

    @Resource
    private DishFlavorService dishFlavorService;

    @Resource
    private CategoryService categoryService;

    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        //构造分页构造器
        Page<Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();

        //构造条件构造器
        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(name != null,Dish :: getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish :: getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records=pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item) -> {
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("修改菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status,Long[] ids){
        //修改状态
        dishService.updateStatus(status,ids);
        return R.success((status==1?"起售":"停售")+"成功");
    }

    @DeleteMapping
    public R<String> delete(Long[] ids){
        dishService.deleteByIdsWithFlavor(ids);
        return R.success("删除成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList=null;
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        //先从redis中获取缓存数据
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList!=null){
            //如果存在，直接返回
            return R.success(dishDtoList);
        }

        LambdaQueryWrapper<Dish> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish :: getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish :: getStatus,1);
        queryWrapper.orderByAsc(Dish :: getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList=list.stream().map((item) -> {
            DishDto dishDto=new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category!=null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor :: getDishId,id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在，需要查询数据库，将查到的菜品数据缓存到redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }

}
