package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private Integer permId;
    private Integer parentId;
    private String name;
    private String path;
    private String iconCls;
    private Integer deleteFlag;
    private Integer orderSort;
    private String description;

    private Meta meta;
    private List<Menu> children;

}
