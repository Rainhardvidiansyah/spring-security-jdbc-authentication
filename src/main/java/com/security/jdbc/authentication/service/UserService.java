package com.security.jdbc.authentication.service;

import com.security.jdbc.authentication.dto.UserInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserService {

    String addUser(String email, String password);

    UserInfo findUserById(Long id);

    List<GrantedAuthority> getAuthoritiesByEmail(String email);
}
