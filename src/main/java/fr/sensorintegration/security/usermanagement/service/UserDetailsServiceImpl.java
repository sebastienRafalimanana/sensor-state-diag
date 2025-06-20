package fr.sensorintegration.security.usermanagement.service;

import fr.sensorintegration.security.usermanagement.data.entity.Account;
import fr.sensorintegration.security.usermanagement.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByUsername(username);
        return account.orElseThrow(()->new UsernameNotFoundException("Account Not Found"));
    }
}
