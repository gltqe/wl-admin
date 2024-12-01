package com.gltqe.wladmin.quartz.task;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component("sysTask")
public class SysTask {
    public void testZero() throws InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        String name = Thread.currentThread().getName();
        System.out.println("testZero..." + format + "......" + name);
    }

    public void testOne(String a) throws InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        String name = Thread.currentThread().getName();
        System.out.println("测试参数:" + a);
        JSONObject object = JSONObject.parseObject(a);
        System.out.println("json:" + object.toJSONString());
        System.out.println("testOne..." + format + "..." + name);
    }

    public void testTwo(String a) throws InterruptedException {
        String name = Thread.currentThread().getName();
        System.out.println(name + "正在执行");
        for (int i = 0; i < 30; i++) {
            Thread.sleep(5000);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = sdf.format(new Date());
            System.out.println("testTwo..." + format + "..." + i + "..." + name);
            System.out.println("测试参数:" + a);
        }
        System.out.println(name + "执行完毕");
    }


}
