package app.credits.configuration;

import app.credits.service.DBUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DBUserDetailsService dbUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(dbUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsManager())
                .passwordEncoder(passwordEncoder())
                .and().build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().and()
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/loan-service/user").permitAll()
                        .requestMatchers(HttpMethod.POST, "/loan-service/getTariffs").permitAll()
                        .requestMatchers("/loan-service/tariff").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/user/me").authenticated()
                        .requestMatchers("/loan-service/user/me/**").authenticated()
                        .requestMatchers("/loan-service/user").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/getUsers").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/user/**").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/order/me").authenticated()
                        .requestMatchers("/loan-service/order").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/order/**").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/getOrders").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/getStatusOrder").hasAuthority("ADMIN")
                        .requestMatchers("/loan-service/**").authenticated()
                        .requestMatchers("**").permitAll()
                )
                .csrf().disable()
                .authenticationManager(authenticationManager(httpSecurity))
                .build();
    }
}
