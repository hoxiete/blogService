package com.project.config;

import com.project.entity.Blog;
import com.project.entity.Image;
import com.project.mapper.BlogMapper;
import com.project.mapper.UploadMapper;
import com.project.util.FastDFSClient;
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
    @Autowired
    private UploadMapper uploadMapper;
    @Autowired
    private FastDFSClient fastDFSClient;

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

    @Test
    public void deleteImg(){


//        List<Image> collect = uploadMapper.selectAll().stream().filter(dto -> dto.getImageType().equals("2")).collect(Collectors.toList());
//       for(Image image : collect){
//           fastDFSClient.deleteBlogImage("group1/M00/00/00/rBMABF8mMISAM_2iAAzQZZpcXNs896.jpg");

//       }
//                .forEach(dto -> {
//                    fastDFSClient.deleteFile(dto.getImageUrl());
////                    uploadMapper.deleteByPrimaryKey(dto.getImageId());
//                });
    }


}
