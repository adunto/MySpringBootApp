package com.basic.myspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:/test.properties")
@SpringBootTest
class MySpringBootAppApplicationTests {

	@Test
	void contextLoads() {
	}

}
