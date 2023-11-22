package be.vinci.ipl.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * The main class for the Order microservice application.
 *
 * <p>This class is annotated with {@code @SpringBootApplication} to enable Spring Boot features and
 * {@code @EnableDiscoveryClient} for service discovery in a microservices environment.
 * It includes the main method to run the Spring Boot application.</p>
 *
 * <p>The application utilizes the {@code @EnableDiscoveryClient} annotation to register itself
 * with the service discovery component, allowing other services to discover and communicate with it.</p>
 *
 * @see SpringBootApplication
 * @see EnableDiscoveryClient
 * @see EnableFeignClients
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderApplication {

    /**
     * The main method to start the Order microservice application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
