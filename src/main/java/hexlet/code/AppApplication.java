package hexlet.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class AppApplication {

    @Autowired
    ConfigurableEnvironment conf;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppApplication.class);
        application.setAdditionalProfiles(System.getenv("SPRING_PROFILES_ACTIVE"));
        application.run(args);
    }
}
