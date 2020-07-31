package com.project.service.impl;

import com.project.entity.Dict;
import com.project.mapper.DictMapper;
import com.project.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public List<Dict> selectDictByType(Dict dict) {
        return dictMapper.select(dict);
    }

    @Override
    public int insertDict(Dict dict) {
        return dictMapper.insert(dict);
    }
}
