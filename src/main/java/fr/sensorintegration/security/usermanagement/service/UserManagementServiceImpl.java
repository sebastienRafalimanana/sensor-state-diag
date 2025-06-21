package fr.sensorintegration.security.usermanagement.service;

import fr.sensorintegration.business.dto.AccountDto;
import fr.sensorintegration.business.mapper.AccountMapper;
import fr.sensorintegration.core.exception.BadRequestException;
import fr.sensorintegration.security.usermanagement.data.entity.Account;
import fr.sensorintegration.security.usermanagement.presentation.response.AuthenticationResponse;
import fr.sensorintegration.security.usermanagement.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        if (accountRepository.existsByUsername(
                accountDto.getUsername())) {
            throw new IllegalArgumentException("Username or email already exists");
        }

        Account account = accountMapper.toEntity(accountDto);
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        account.setEnabled(false);

        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDto(savedAccount);
    }

    @Override
    public AuthenticationResponse signIn(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.debug(authentication.toString());
            if (authentication.isAuthenticated()){
                Instant instant = Instant.now();
                String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
                JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                        .issuedAt(instant)
                        .expiresAt(instant.plus(24, ChronoUnit.HOURS))
                        .subject(username)
                        .claim("scope",scope)
                        .build();

                JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS512).build(),
                        jwtClaimsSet
                );
                String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
                return AuthenticationResponse.builder().accessToken(jwt).build();
            }
        }
        catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
        throw new BadRequestException("Invalid username or password");
    }

    @Override
    public boolean activateAccount(String activationToken) {
        return true;
    }
}