package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogShowDto {
    private Integer id;
    private String title;
    private Integer typeId;
    private Integer userId;
    private String body;
    private String bodyHtml;
    private String imgs;
    private Integer deleteFlag;
    private String createUser;
    private Date createTime;
    private String updateUser;
    private Date updateTime;
    private String userName;
}
