package fr.sensorintegration.security.usermanagement.presentation.response;

import fr.sensorintegration.business.dto.AccountDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
}
