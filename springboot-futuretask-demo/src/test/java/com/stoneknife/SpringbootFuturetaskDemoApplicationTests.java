package com.stoneknife;

import com.stoneknife.service.UserService;
import com.stoneknife.service.UserService_FutureTask;
import com.stoneknife.service.UserService_MyFutureTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootFuturetaskDemoApplicationTests {

   /* @Autowired
    UserService userService;*/

   /* @Autowired
    UserService_FutureTask userService;*/


   @Autowired
   UserService_MyFutureTask userService;

    @Test
    void contextLoads() {
        long beginTime = System.currentTimeMillis();
        Object o = userService.getUserInfo();
        long time = System.currentTimeMillis() - beginTime;
        System.out.println(o);
        System.out.println("总耗时" + time);
    }

}
