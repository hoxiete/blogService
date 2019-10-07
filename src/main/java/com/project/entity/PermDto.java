package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ClassName: PermDto
 * Function:  TODO  菜单相关dto
 * Date:      2019/10/2 21:19
 * author     Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermDto {
    private Integer permId;
    private String label;
    List<PermDto> children;
}
