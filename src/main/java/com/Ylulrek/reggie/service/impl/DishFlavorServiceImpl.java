package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.domain.DishFlavor;
import com.Ylulrek.reggie.mapper.DishFlavorMapper;
import com.Ylulrek.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
