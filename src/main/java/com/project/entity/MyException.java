package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyException extends RuntimeException{

    private int status;  //异常状态码

    private String message;  //异常信息

    private String method;   //发生的方法，位置等

   public MyException(int status,String message){
       this.status = status;
       this.message = message;
    }
}