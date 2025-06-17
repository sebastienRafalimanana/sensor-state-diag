package fr.sensorintegration.security.usermanagement.service;

import fr.sensorintegration.business.dto.AccountDto;
import fr.sensorintegration.security.usermanagement.presentation.response.AuthenticationResponse;

public interface UserManagementService {

    AccountDto createAccount(AccountDto accountDto);

    AuthenticationResponse signIn(String username, String password);

    boolean activateAccount(String activationToken);
}