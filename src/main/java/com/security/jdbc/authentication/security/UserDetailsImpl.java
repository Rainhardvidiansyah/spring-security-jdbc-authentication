package com.security.jdbc.authentication.security;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@ToString
public class UserDetailsImpl implements UserDetails {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsImpl.class);

    private Long id;

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean enabled;

    public UserDetailsImpl(Long id, String email, String password,
                           Collection<? extends GrantedAuthority> authorities, boolean enabled) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public void loggingUserData(){
        log.info("Email user:{} ", this.email);
        log.info("Password user:{} ", this.password);
        log.info("Role user:{} ", this.authorities);
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
