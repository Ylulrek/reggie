package com.Ylulrek.reggie.service;

import com.Ylulrek.reggie.domain.Orders;
import com.baomidou.mybatisplus.extension.service.IService;


public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);

}
