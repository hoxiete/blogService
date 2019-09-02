package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sys_user")
public class User implements Serializable {
    private Integer userId;
    private Integer roleId;
    private String userName;
    private String loginName;
    private String tel;
    private String passWord;


}
