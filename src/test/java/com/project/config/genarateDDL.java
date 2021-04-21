package com.project.config;


import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.ClassScaner;
import com.google.common.collect.ImmutableMap;
import com.project.entity.*;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/***
 * 通过java Class 创建 建表语句
 */
public class genarateDDL {
    public static ImmutableMap<Object, Object> javaProperty2SqlColumnMap = ImmutableMap
            .builder()
            .put("Integer", "INTEGER")
            .put("Short", "tinyint")
            .put("Long", "bigint")
            .put("BigDecimal", "decimal(19,2)")
            .put("Double", "double precision not null")
            .put("Float", "float")
            .put("Boolean", "bit")
            .put("Date", "datetime")
            .put("String", "VARCHAR(50)")
            .build();
    public static Map<String, String> fieldCommentColumnMap =
            ImmutableMap.of("delete_flag","删除标志",
                    "create_user","创建用户",
                    "create_time","创建时间",
                    "update_user","更新用户",
                    "update_time","更新时间");

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String packageName = "com.project.entity";
        List<Class<?>> entitys = findEntity(packageName);
        entitys.stream().map(genarateDDL::createTable).forEach(System.out::println);
    }

    private static List<Class<?>> findEntity(String packageDirName) throws IOException {
        Set<Class<?>> classes = ClassScaner.scanPackage(packageDirName);
       return classes.stream()
                .filter(c ->
                        Arrays.stream(AnnotationUtil.getAnnotations(c, true))
                                .anyMatch(i ->
                                        i.annotationType().equals(Table.class)))
                .collect(Collectors.toList());
    }

    public static String createTable(Class obj){
        Field[] fields = null;
        String tableName;
        fields = obj.getDeclaredFields();
        String param = null;
        String column = null;
        StringBuilder sb = null;
        sb = new StringBuilder(50);
        tableName = (String) AnnotationUtil.getAnnotationValueMap(obj, Table.class).get("name");
        sb.append("create table ").append(tableName).append(" ( \r\n ");
        for (Field f : fields) {
            column = f.getName();
            if (column.equals("serialVersionUID")) {
                continue;
            }
            param = f.getType().getSimpleName();
            //大小写切换
            StringBuffer stringBuffer = new StringBuffer();
            for (char c : column.toCharArray()) {
                if(Character.isUpperCase(c)){
                    stringBuffer.append("_");
                    stringBuffer.append(Character.toLowerCase(c));
                }else {
                    stringBuffer.append(c);
                }
            }
            sb.append(stringBuffer.toString());
            sb.append(" ").append(javaProperty2SqlColumnMap.get(param)).append(" ");
            Annotation[] annotation = AnnotationUtil.getAnnotations(f,true);
            if(Arrays.stream(annotation).anyMatch(a -> a.annotationType().equals(Id.class))){
                sb.append(" NOT NULL primary key AUTO_INCREMENT comment 'primary key',\n ");
            }else{
                sb.append(" COMMENT '").append(fieldCommentColumnMap.get(stringBuffer.toString())).append("',\n ");
            }
        }
        String sql = null;
        sql = sb.toString();
        //去掉最后一个逗号
        int lastIndex = sql.lastIndexOf(",");
        sql = sql.substring(0, lastIndex) + sql.substring(lastIndex + 1);
        sql = sql.substring(0, sql.length() - 1) + ")ENGINE =INNODB DEFAULT  CHARSET= utf8;\r\n";
        return sql;
    }
}