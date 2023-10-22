package hexlet.code.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
@ComponentScan({
    "hexlet.code"
})
@RequiredArgsConstructor
public class RollbarConfig {

    @Value("${rollbar.token}")
    private String rollbarToken;
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfigs(rollbarToken));
    }

    private Config getRollbarConfigs(String accessToken) {

        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
            .environment("development")
            .enabled(activeProfile == "production")
            .build();
    }
}
