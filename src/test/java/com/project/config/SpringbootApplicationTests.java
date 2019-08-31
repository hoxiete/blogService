package com.project.config;

import com.project.util.AESUtil;
import com.project.util.MD5Utils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApplicationTests {

    @Test
    public void contextLoads() {
         String m = MD5Utils.md5("123456");
        System.out.println(m);
    }

}
