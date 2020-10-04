package com.dhitha.springbootoauthserver;

import com.dhitha.springbootoauthserver.oauth.dto.OpenIdConfigurationDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(OpenIdConfigurationDTO.class)
public class SpringBootOauthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootOauthServerApplication.class, args);
	}

}
