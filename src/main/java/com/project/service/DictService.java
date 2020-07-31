package com.project.service;

import com.project.entity.Dict;

import java.util.List;

public interface DictService {

    public List<Dict> selectDictByType(Dict dict);

    public int insertDict(Dict dict);
}
