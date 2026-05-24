package giuseppetavella.zero_chiamate;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class App {
	


	public static void main(String[] args) {

		SpringApplication.run(App.class, args);
	}
	


	/**
	 * This bean was used to check whether Flyway dependency exists.
	 * There were issues with Flyway, due to Spring Boot not autoconfiguring it.
	 * So Flyway was in the classpath, but the bean was not there.
	 * 
	 * Solution: we manually configured the Flyway bean.
	 * 
	 * If you ever need to check for Flyway existence/correct configuration,
	 * you can use this bean to do that.
	 */
	// @Bean
	// public ApplicationRunner flywayCheck(ApplicationContext context) {
	// 	return args -> {
	// 		try {
	// 			Class.forName("org.flywaydb.core.Flyway");
	// 			System.out.println(">>> FLYWAY IS ON CLASSPATH");
	// 		} catch (ClassNotFoundException e) {
	// 			System.out.println(">>> FLYWAY NOT FOUND ON CLASSPATH");
	// 		}
	//
	// 		try {
	// 			context.getBean("flyway");
	// 			System.out.println(">>> FLYWAY BEAN EXISTS");
	// 		} catch (Exception e) {
	// 			System.out.println(">>> FLYWAY BEAN NOT FOUND: " + e.getMessage());
	// 		}
	// 	};
	// }

}
