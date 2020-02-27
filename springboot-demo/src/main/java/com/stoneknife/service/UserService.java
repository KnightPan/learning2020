package com.stoneknife.service;

import com.stoneknife.annotation.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Object find(long userId) {
        String sql = "select * from t_user where id=" + userId;
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return result;
    }


    @RedisCache(key="#id")
    public Object queryById(String id){
        System.out.println("查询语句");
        return "111";
    }

    @RedisCache(key="'hello:'")
    public Object query(){
        System.out.println("查询语句222");
        return "222";
    }
}
