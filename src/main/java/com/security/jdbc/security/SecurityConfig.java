package com.security.jdbc.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    @Autowired
    private DataSource dataSource;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/api/v1/users/auth/**").permitAll()
                        .requestMatchers("/api/v1/public/**").permitAll()
                        .anyRequest().authenticated());
    return httpSecurity.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

//    @Bean
//    public JdbcDaoImpl userDetailsService(DataSource dataSource) {
//        JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
//        if (dataSource != null) {
//            jdbcDaoImpl.setDataSource(dataSource);
//        } else {
//            throw new RuntimeException("DataSource is null!");
//        }
//        return jdbcDaoImpl;
//    }


    //Below code works!
    /*
        @Bean
    public UserDetailsService userDetailsService(){
        String retrievedUser = "SELECT email as username, password, enabled from users where email = ?";
        String retrievedAuthorities = "SELECT u.email as username, a.authorities_name as authority " +
                "FROM user_authorities ua " +
                "JOIN users u ON ua.user_id = u.id " +
                "JOIN authorities a ON ua.authorities_id = a.id " +
                "WHERE u.email = ?";

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        jdbcUserDetailsManager.setUsersByUsernameQuery(retrievedUser);
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(retrievedAuthorities);
        return jdbcUserDetailsManager;
    }
     */


}
