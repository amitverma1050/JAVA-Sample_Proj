package com.sampleProj.demo;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleJobAppApplication {

	static {
		System.setProperty("user.timezone", "Asia/Kolkata");
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
	}

	public static void main(String[] args) {
		SpringApplication.run(SampleJobAppApplication.class, args);
	}
}
