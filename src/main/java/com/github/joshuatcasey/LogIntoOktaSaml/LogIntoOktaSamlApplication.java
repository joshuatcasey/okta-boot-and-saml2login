package com.github.joshuatcasey.LogIntoOktaSaml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Saml2RelyingPartyMappings.class)
public class LogIntoOktaSamlApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogIntoOktaSamlApplication.class, args);
    }

}
