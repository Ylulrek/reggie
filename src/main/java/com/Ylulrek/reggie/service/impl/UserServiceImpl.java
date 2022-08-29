package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.domain.User;
import com.Ylulrek.reggie.mapper.UserMapper;
import com.Ylulrek.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
