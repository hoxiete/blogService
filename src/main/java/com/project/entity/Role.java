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
@Table(name = "t_sys_role")
public class Role {
    @Id
    private Integer id;
    private String name ;
    private String description;
    private Integer orderSort;
    private Integer deleteFlag;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;

}
