package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * ClassName: InterviewEntity
 * Function:  TODO  用户访问操作实体
 * Date:      2019/11/25 20:28
 * author     Administrator
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_sys_interview")
public class InterviewEntity implements Serializable {
    private static final long serialVersionUID = -5645108301917994003L;
    @Id
    private Integer id;

    private String userName;
    //运行时间
    private Long runTime;
    //访问的位置
    private String state;
//    //信息
//    private String message;

    private String ip;

    private Date recordTime;

}
