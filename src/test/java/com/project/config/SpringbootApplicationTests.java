package com.project.config;

import com.project.entity.Blog;
import com.project.mapper.BlogMapper;
import com.project.util.AESUtil;
import com.project.util.MD5Utils;
import me.zhyd.hunter.config.HunterConfig;
import me.zhyd.hunter.config.HunterConfigContext;
import me.zhyd.hunter.config.platform.Platform;
import me.zhyd.hunter.entity.VirtualArticle;
import me.zhyd.hunter.enums.ExitWayEnum;
import me.zhyd.hunter.processor.BlogHunterProcessor;
import me.zhyd.hunter.processor.HunterProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {
    @Autowired
    private BlogMapper blogMapper;

    @Test
    public void contextLoads() {
        HunterConfig config = HunterConfigContext.getHunterConfig(Platform.CSDN);
        config.setUid("zhhao1")
                .setExitWay(ExitWayEnum.DURATION)
                .setCount(48);
        HunterProcessor hunter = new BlogHunterProcessor(config);
        System.out.println("程序开始执行：" + new Date());
        CopyOnWriteArrayList<VirtualArticle> list = hunter.execute();
        System.out.println("程序执行完毕：" + new Date());

        list.stream().forEach(blog -> {
            int length = blog.getDescription().length() > 100 ? 100 : blog.getDescription().length();
            Blog insertblog =  Blog.builder().bodyHtml(blog.getContent()).title(blog.getTitle()).updateUser("clawl").createUser("clawl")
                   .summary(blog.getDescription().substring(0,length)).createTime(new Date()).updateTime(new Date()).build();

            blogMapper.insert(insertblog);
        });
    }


}
