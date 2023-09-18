package com.emintufan.orderservice.config;

import com.emintufan.orderservice.dao.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    private UserRepository userRepository;

    @Autowired
    public AppConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration configuration) throws Exception {
        return (configuration.getAuthenticationManager());
    }
    @Bean
    public PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder());
        provider.setUserDetailsService(userDetailService());
        return (provider);
    }
    @Bean
    public UserDetailsService userDetailService() {
        return (loadUserByUsername -> userRepository.findByEmail(loadUserByUsername)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found !")));
    }
}
