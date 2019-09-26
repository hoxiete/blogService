package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sys_permission")
public class Router {
    @Id
    private Integer permId;
    private String name;
    private String path;
    private Integer parentId;
    private Integer orderSort;
    private Integer isButton;
    private String iconCls;
    private String description;
    private Integer deleteFlag;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;

}
