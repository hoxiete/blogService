package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * ClassName: BmsCorsConfiguration
 * Function:  TODO
 * Date:      2019/6/27 15:31
 * author     zhh
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        //初始化cors配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        //设置允许跨域的域名
        corsConfiguration.addAllowedOrigin("*");
        //设置可以携带cookie
        corsConfiguration.setAllowCredentials(true);
        //允许所有请求方法
        corsConfiguration.addAllowedMethod("*");
        //设置头部信息
        corsConfiguration.addAllowedHeader("*");

        //初始化cors配置源对象
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",corsConfiguration);

        //返回cors实例
        return  new CorsFilter(configurationSource);
    }
}