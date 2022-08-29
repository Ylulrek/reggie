package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.domain.Employee;
import com.Ylulrek.reggie.mapper.EmployeeMapper;
import com.Ylulrek.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
