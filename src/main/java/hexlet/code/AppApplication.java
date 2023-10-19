package hexlet.code;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Task Manager",
        version = "1.0",
        description = "Manage your tasks!"
    )
)
@SecurityScheme(name = "javainuseapi", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class AppApplication {

    @Autowired
    ConfigurableEnvironment conf;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AppApplication.class);
        String profile = System.getenv("SPRING_PROFILES_ACTIVE") == null ? "development"
            : System.getenv("SPRING_PROFILES_ACTIVE");
        application.setAdditionalProfiles(profile);
        application.run(args);
    }
}
