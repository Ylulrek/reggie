package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.domain.ShoppingCart;
import com.Ylulrek.reggie.mapper.ShoppingCartMapper;
import com.Ylulrek.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
