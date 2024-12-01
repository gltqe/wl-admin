package com.gltqe.wladmin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gltqe
 * @date 2024/7/24 16:34
 */
@Slf4j
@SpringBootTest
public class LogTest {

    @Test
    public void test(){
        log.debug("debug");
        log.info("info");
        log.warn("warn");
        log.error("error");
    }
}
