package cn.offway.hades;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableAdminServer
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.vinann", "cn.offway"})
public class HadesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HadesApplication.class, args);
    }
}
