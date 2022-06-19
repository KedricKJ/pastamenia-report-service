package com.pastamenia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.pastamenia")
public class PastameniaReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(PastameniaReportApplication.class, args);
	}

}
