package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.domain.OrderDetail;
import com.Ylulrek.reggie.mapper.OrderDetailMapper;
import com.Ylulrek.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
