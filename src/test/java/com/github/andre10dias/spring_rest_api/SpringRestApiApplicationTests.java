package com.github.andre10dias.spring_rest_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // ← isso usa application-test.yml
class SpringRestApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
