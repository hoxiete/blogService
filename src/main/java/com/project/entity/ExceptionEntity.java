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
@Table(name = "t_sys_exception")
public class ExceptionEntity {
    @Id
    private Integer id;

    private String userName;

    private Integer status;  //异常状态码

    private String message;  //异常信息

    private String method;   //发生的方法，位置等

    private String url;

    private String ip;

    private Date recordTime;
}
