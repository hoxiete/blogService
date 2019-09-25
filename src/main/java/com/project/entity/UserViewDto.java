package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserViewDto {
    private Integer userId;
    private Integer roleId;
    private String roleName;
    private String userName;
    private String loginName;
    private String tel;
    private String passWord;
    private String birthDay;
    private Integer sex;
    private Integer deleteFlag;

}
