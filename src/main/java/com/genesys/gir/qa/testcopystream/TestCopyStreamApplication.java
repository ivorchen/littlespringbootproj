package com.genesys.gir.qa.testcopystream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class TestCopyStreamApplication {

	private static Logger logger= LogManager.getLogger(TestCopyStreamApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(TestCopyStreamApplication.class, args);
	}


}
