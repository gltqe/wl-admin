package com.gltqe.wladmin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class WlAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(WlAdminApplication.class, args);
        log.info("wl-admin 已启动");
    }

}
