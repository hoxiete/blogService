package com.project.entity;

import lombok.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sys_user")
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -6309591275732240138L;
    @Id
    private Integer userId;
    private Integer roleId;
    private String userName;
    private String loginName;
    private String email;
    private String tel;
    private String passWord;
    private String headimg;
    private String birthDay;
    private Integer sex;
    private String bz;
    private Integer deleteFlag;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;




}
