package fr.sensorintegration.security.usermanagement.presentation;

import fr.sensorintegration.business.dto.AccountDto;
import fr.sensorintegration.core.model.ResponseModel;
import fr.sensorintegration.security.usermanagement.presentation.request.AuthenticationReq;
import fr.sensorintegration.security.usermanagement.presentation.response.AuthenticationResponse;
import fr.sensorintegration.security.usermanagement.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController("account")
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

    @PostMapping(path = "/sign-in")
    ResponseEntity<ResponseModel<AuthenticationResponse>> signIn(@RequestBody AuthenticationReq authenticationReq) {
        AuthenticationResponse authenticationResponse = userManagementService.signIn(authenticationReq.username(), authenticationReq.password());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.<AuthenticationResponse>builder()
                .message("Sign in successfully")
                .success(true)
                .data(authenticationResponse)
                .build());
    }
}
