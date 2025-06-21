package fr.sensorintegration.security.usermanagement.presentation;

import fr.sensorintegration.business.dto.AccountDto;
import fr.sensorintegration.core.model.ResponseModel;
import fr.sensorintegration.security.usermanagement.presentation.request.AuthenticationReq;
import fr.sensorintegration.security.usermanagement.presentation.response.AuthenticationResponse;
import fr.sensorintegration.security.usermanagement.service.UserManagementService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Tag(name = "Account management", description = "Controller for Account")
public class AccountController {
    private final UserManagementService userManagementService;

    @PostMapping(path = "create")
    ResponseEntity<ResponseModel<AccountDto>> createAccount(@RequestBody AccountDto accountDto) {
        AccountDto account = userManagementService.createAccount(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseModel.<AccountDto>builder()
                .message("Account created successfully")
                .success(true)
                .data(account)
                .build());
    }

    @PostMapping(path = "/signIn")
    ResponseEntity<ResponseModel<AuthenticationResponse>> signIn(@RequestBody AuthenticationReq authenticationReq) {
        log.info("Sign in request received for username: {}", authenticationReq.username());
        AuthenticationResponse authenticationResponse = userManagementService.signIn(authenticationReq.username(), authenticationReq.password());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.<AuthenticationResponse>builder()
                .message("Sign in successfully")
                .success(true)
                .data(authenticationResponse)
                .build());
    }
}
