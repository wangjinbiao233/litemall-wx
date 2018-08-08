package org.linlinjava.litemall.admin.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    //@Scheduled(cron = "0/1 * * * * ?") // 每10分钟执行一次
    public void execute() {
        //System.out.print(LocalDateTime.now() + " -- execute定时任务 \n");
    }
}
