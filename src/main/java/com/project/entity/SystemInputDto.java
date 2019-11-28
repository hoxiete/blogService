package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ClassName: SystemInputDto
 * Function:  异常类接受dto
 * Date:      2019/10/30 19:48
 * author     Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemInputDto {

    private Integer id;

    private String userName;

    private String startTime;

    private String endTime;

    private Integer resolveFlag;
}
