package com.Ylulrek.reggie.service;

import com.Ylulrek.reggie.domain.Setmeal;
import com.Ylulrek.reggie.dto.SetmealDto;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);

    public void deleteByIdsWithDish(Long[] ids);

    public void updateStatus(Integer status, Long[] ids);
}
