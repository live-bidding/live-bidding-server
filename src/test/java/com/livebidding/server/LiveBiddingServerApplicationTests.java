package com.livebidding.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(properties = {
    "jwt.secret=test-secret-key-for-live-bidding-application-test-1234567890",
    "jwt.access-token-expire-time=1000",
    "jwt.refresh-token-expire-time=2000"
})
@SpringBootTest
class LiveBiddingServerApplicationTests {

	@Test
	void contextLoads() {
	}

}
