package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ClassName: Assign
 * Function:  TODO  角色权限分配
 * Date:      2019/10/2 10:28
 * author     Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_role_permission")
public class Assign {
    @Id
    private Integer id;
    private Integer roleId;
    private Integer permId;
    private Integer deleteFlag;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;

}
