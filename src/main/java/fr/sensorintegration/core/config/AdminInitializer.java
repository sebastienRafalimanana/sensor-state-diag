package fr.sensorintegration.core.config;

import fr.sensorintegration.business.dto.AccountDto;
import fr.sensorintegration.core.enums.Role;
import fr.sensorintegration.security.usermanagement.entity.Account;
import fr.sensorintegration.security.usermanagement.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserManagementService userManagementService;

    @Override
    public void run(String... args) throws Exception {
        AccountDto adminAccount = AccountDto.builder()
                .username("admin")
                .password("admin123")
                .firstname("Administrator")
                .lastname("Administrator")
                .role(Role.ADMINISTRATOR)
                .build();
        try {
            userManagementService.createAccount(adminAccount);
            log.info("Default administrator created successfully. Username: admin, Password: admin123");
        } catch (Exception e) {
            log.warn("Default administrator already exists.");
        }
    }
}
