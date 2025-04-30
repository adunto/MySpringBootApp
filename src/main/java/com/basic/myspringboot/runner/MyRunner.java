package com.basic.myspringboot.runner;

import com.basic.myspringboot.CustomVO;
import com.basic.myspringboot.property.MyBootProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@Component
public class MyRunner implements ApplicationRunner {
    @Value("${myboot.name}")
    private String name;
    @Value("${myboot.age}")
    private int age;
    @Value("${myboot.fullName}")
    private String fullName;

    @Autowired
    private Environment environment;
    @Autowired
    private MyBootProperties myBootProperties;
    @Autowired
    private CustomVO customVO;

    private Logger logger = LoggerFactory.getLogger(MyRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ch.qos.logback.classic.Logger
        System.out.println("Logger 구현체 : " + logger.getClass().getName());
        System.out.println("VM Argument foo : " + args.containsOption("foo"));

        System.out.println("Program Argument bar : " + args.containsOption("bar"));

        logger.debug("${myboot.name} = " + myBootProperties.getName());
        logger.debug("${myboot.age} = " + myBootProperties.getAge());
        logger.debug("${myboot.fullName} = " + myBootProperties.getFullName());

        logger.info("설정된 Port 번호 " + environment.getProperty("local.server.port"));
        logger.info("현재 활성화 된 CustomVO = " + customVO);
    }
}
