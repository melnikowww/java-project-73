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
//        String profile = isProd() ? "production" : "development";
//        application.setAdditionalProfiles(profile);
        application.run(args);
    }
}
