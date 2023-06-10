package com.greenjiao.coc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.greenjiao.coc.bean.User;
import com.greenjiao.coc.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CocApplicationTests {

    @Test
    void contextLoads() {
    }
    @Autowired
    private UserMapper userMapper;
    @Test
    void userTest(){
        QueryWrapper<User> select = new QueryWrapper<User>();
        select.select("id");
        List<User> users = userMapper.selectList(select);
        System.out.println(users);
    }
    @Test
    void mybatisTest(){
        User user = new User().setAccount("name");
        String idStr = IdWorker.getIdStr(user);
        System.out.println(idStr);
    }

}
