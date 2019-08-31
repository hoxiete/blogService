package com.project.config;

import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

@Component
public interface BaseMapper extends Mapper, MySqlMapper {
}
