package fr.sensorintegration.business.dto;

import fr.sensorintegration.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Role role;
}
