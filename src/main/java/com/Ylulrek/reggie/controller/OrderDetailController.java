package com.Ylulrek.reggie.controller;

import com.Ylulrek.reggie.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/orderDetail")
@Slf4j
public class OrderDetailController {

    @Resource
    private OrderDetailService orderDetailService;



}
