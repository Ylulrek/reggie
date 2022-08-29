package com.Ylulrek.reggie.controller;

import com.Ylulrek.reggie.common.R;
import com.Ylulrek.reggie.domain.Orders;
import com.Ylulrek.reggie.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    private OrderService orderService;

    //用户下单
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("orders数据{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize){
        //构造分页构造器
        Page<Orders> pageInfo=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
        //构造分页构造器
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }
}
