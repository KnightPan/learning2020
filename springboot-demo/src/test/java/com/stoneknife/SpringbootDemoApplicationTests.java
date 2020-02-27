package com.stoneknife;

import com.stoneknife.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootDemoApplicationTests {

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        Object o = userService.find(2);
        System.out.println(o);
    }

    @Test
    void queryById(){
        Object o = userService.queryById("1234");
        System.out.println(o);
    }

    @Test
    void query(){
        Object o = userService.query();
        System.out.println(o);
    }
}
