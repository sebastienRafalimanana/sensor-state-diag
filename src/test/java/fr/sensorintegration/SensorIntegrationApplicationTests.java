package fr.sensorintegration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
        })
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class SensorIntegrationApplicationTests {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
        // This validates that all beans can be instantiated without errors
    }

    @Test
    void applicationStarts() {
        // Test that the application can start without throwing exceptions
        // This is a basic smoke test
    }

}
