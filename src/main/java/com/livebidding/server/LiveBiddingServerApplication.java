package com.livebidding.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LiveBiddingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveBiddingServerApplication.class, args);
	}

}
