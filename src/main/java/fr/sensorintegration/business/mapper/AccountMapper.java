package fr.sensorintegration.business.mapper;

import fr.sensorintegration.business.dto.AccountDto;
import fr.sensorintegration.security.usermanagement.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDto toDto(Account entity) {
        if (entity == null) {
            return null;
        }
        return AccountDto.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .role(entity.getRole())
                .build();
    }

    public Account toEntity(AccountDto dto) {
        if (dto == null) {
            return null;
        }
        return Account.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .role(dto.getRole())
                .build();
    }
}
