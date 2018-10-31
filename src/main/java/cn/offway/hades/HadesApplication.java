package cn.offway.hades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.vinann","cn.offway"})
public class HadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(HadesApplication.class, args);
	}
}
