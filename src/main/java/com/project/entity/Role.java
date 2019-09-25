package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sys_role")
public class Role {
    @Id
    private Integer id;
    private String name ;
    private String description;
    private String orderSort;

}
