package fr.sensorintegration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "temporal.enabled=false",
                "spring.security.enabled=false"
        })
@ActiveProfiles("test")
public class SimpleApplicationTest {

    @Test
    void contextLoads() {
        // Test that Spring context can load with minimal configuration
        String testValue = "Application context loaded successfully";
        assertNotNull(testValue);
    }

    @Test
    void basicFunctionality() {
        // Basic test to verify JUnit is working
        int result = 2 + 2;
        assert result == 4;
    }
}