package br.com.unip.gamerating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class GameratingApplication {

	public static void main(String[] args) {
		System.setProperty("vaadin.whitelisted-packages", "br/com/unip/");
		SpringApplication.run(GameratingApplication.class, args);
	}

}
