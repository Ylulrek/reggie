package com.Ylulrek.reggie.service.impl;

import com.Ylulrek.reggie.domain.AddressBook;
import com.Ylulrek.reggie.mapper.AddressBookMapper;
import com.Ylulrek.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
