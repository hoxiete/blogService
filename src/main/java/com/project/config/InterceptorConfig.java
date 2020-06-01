package com.project.config;


import com.project.config.filter.RestfulFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(restfulFilter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //本地资源映射至服务器端口，解决浏览器不支持访问硬盘文件
        registry.addResourceHandler("/image/**").addResourceLocations("file:D://baseUrl//headImg/");

    }

}
