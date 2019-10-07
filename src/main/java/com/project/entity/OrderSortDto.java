package com.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderSortDto {

    private Integer moveId;
    private Integer placeId;
    private Integer isBefore;
    private Integer isChangeParentId;

}
