package com.security.jdbc.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jdbc.pojos.UserInfo;
import com.security.jdbc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailsServiceImpl
        implements UserDetailsService {


    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username == null || username.isEmpty()){
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        UserInfo userInfo = userRepository.findOneUser(username);
        List<GrantedAuthority> authorities = userRepository.getUserAuthoritiesByUserEmail(userInfo.getEmail());

        return new UserDetailsImpl(userInfo.getEmail(), userInfo.getPassword(), authorities, userInfo.isEnabled());
    }
}
