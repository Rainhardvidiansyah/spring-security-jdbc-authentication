package com.security.jdbc.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
public class UserDetailsImpl implements UserDetails {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsImpl.class);

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    private boolean enabled;

    public UserDetailsImpl(String email, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled) {
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
