package com.basic.myspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"com.basic.myspringboot"})
public class MySpringBootAppApplication {

	public static void main(String[] args) {
//		SpringApplication.run(MySpringBootAppApplication.class, args);

		SpringApplication application = new SpringApplication((MySpringBootAppApplication.class));
		// 기본적으로 WebApplicationType 은 웹어플리케이션 SERVLET 이다.
		application.setWebApplicationType(WebApplicationType.SERVLET);
		application.run(args);

	}

}

