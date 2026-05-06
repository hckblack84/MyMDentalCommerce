package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.Model.UserEntity;
import com.MyMDentis.MyMDentistComerce.Repository.UserEntityRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public CustomUserDetailsService(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findByEmailUser(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuario " + username + " no encontrado"));

        return User.withUsername(userEntity.getEmailUser())
                .password(userEntity.getPasswordUser())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + userEntity.getRole().name())
                ))
                .build();
    }

    public UserDetails loadByEmailUser(String emailUser) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByEmailUser(emailUser)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + emailUser + " not found in system"));

        return User.withUsername(user.getEmailUser())
                .password(user.getPasswordUser())
                .authorities(Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                ))
                .build();
    }

}