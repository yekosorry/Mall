package com.notime.mall.pay;

import com.alipay.api.AlipayClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MallPayApplicationTests {

    @Autowired
    AlipayClient alipayClient;

    @Test
    public void contextLoads() {

        System.err.println(alipayClient);
    }

}
