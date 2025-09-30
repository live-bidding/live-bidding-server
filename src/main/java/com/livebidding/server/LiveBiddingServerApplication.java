package com.livebidding.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LiveBiddingServerApplication {

	/**
	 * Application entry point that starts the Spring Boot application.
	 *
	 * Invokes SpringApplication.run to initialize the application context, perform auto-configuration,
	 * and start the embedded server for LiveBiddingServerApplication.
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(LiveBiddingServerApplication.class, args);
	}

}
