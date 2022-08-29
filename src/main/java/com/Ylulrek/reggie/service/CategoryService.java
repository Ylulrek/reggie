package com.Ylulrek.reggie.service;

import com.Ylulrek.reggie.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
