package project.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"project.api.rest.model"})
@ComponentScan(basePackages = {"project.*"})
@EnableJpaRepositories(basePackages = {"project.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
public class ProjectspringrestapiApplication implements WebMvcConfigurer{

	public static void main(String[] args) {
		SpringApplication.run(ProjectspringrestapiApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("1234"));
	}
	
	/*Global Mapping that reflects on all system*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/user/**")
		.allowedMethods("*")
		.allowedOrigins("*");
		/*Setting the user mapping to all origins*/
	}

}
