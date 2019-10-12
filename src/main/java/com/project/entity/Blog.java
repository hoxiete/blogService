package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_sys_blog")
public class Blog {
//    private String abstracts;
//    private String abstractImages;
    @Id
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

}
