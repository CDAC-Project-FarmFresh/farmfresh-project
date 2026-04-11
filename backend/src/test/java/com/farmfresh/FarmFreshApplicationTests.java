package com.farmfresh;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = FarmFreshApplication.class)
@ActiveProfiles("test")
class FarmFreshApplicationTests {

	@Test
	void contextLoads() {
	}
}
