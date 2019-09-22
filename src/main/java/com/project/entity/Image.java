package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ClassName: Image
 * Function:  TODO  图片实体
 * Date:      2019/9/21 17:15
 * author     Administrator
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_sys_image")
public class Image {
    /**
     * 表ID，主键，供其他表做外键
     */
    @Id
    private Long imageId;

    /**
     * 依赖ID，用于关联其他表
     */

    private Long recourseId;

    /**
     * 图片类型
     */

    private String imageType;


    /**
     * 图片地址
     */
    private String imageUrl;
    /**
     * 启用标识：1/0
     */

    private Integer deleteFlag;

    /**
     * 创建人
     */

    private String createUser;

    /**
     * 创建时间
     */

    private String createTime;

    /**
     * 更新人
     */

    private String updateUser;

    /**
     * 更新时间
     */

    private Date updateTime;


}
