package com.proyect.tienda.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() throws Exception {
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("admin")
                .password("1234")
                .roles("ADMIN", "USER")
                .build();
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests((requests) -> {
                    try {
                        requests
                                .requestMatchers("/abonar/**").permitAll() // Permitir acceso a /abonar/** sin autenticación
                                .requestMatchers("/editar/**", "/agregar/**", "/eliminar/**").hasRole("ADMIN")
                                .requestMatchers("/").hasRole("USER")
                                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
                                .and()
                                .formLogin(form -> form
                                        .loginPage("/login")
                                        .permitAll());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        http.exceptionHandling((exception) -> exception
                .accessDeniedPage("/errores/403"));
        return http.build();
    }
}
