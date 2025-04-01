package org.helloworld.gymmate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GymmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymmateApplication.class, args);
	}

}
