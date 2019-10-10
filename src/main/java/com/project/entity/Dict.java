package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: Dict
 * Function:   字典表数据实体
 * Date:      2019/10/9 19:10
 * author     Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dict {
    private Integer dictId;
    private String dictType;
    private Integer value;
    private String label;

}
