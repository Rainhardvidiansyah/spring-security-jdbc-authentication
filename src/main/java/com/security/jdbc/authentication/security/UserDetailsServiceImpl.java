package com.security.jdbc.authentication.security;


import com.security.jdbc.authentication.dto.UserInfo;
import com.security.jdbc.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    private final UserRepository userRepository;



    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username == null || username.isEmpty()){
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }

        UserInfo userInfo = userRepository.findOneUserByEnabledAndEmail(username);
        List<GrantedAuthority> authorities = userRepository.getUserAuthoritiesByUserEmail(userInfo.getEmail());

        return new UserDetailsImpl(userInfo.getEmail(), userInfo.getPassword(), authorities, userInfo.isEnabled());
    }
}
