package be.vinci.ipl.matching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Main class for the Matching application.
 * This class configures and launches the Spring Boot application.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MatchingApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(MatchingApplication.class, args);
    }

}