package com.security.jdbc.authentication.repository;

import com.security.jdbc.authentication.dto.UserInfo;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface UserRepository {

    void addUser(String email, String password);

    UserInfo findOneUserByEnabledAndEmail(String email);

    List<GrantedAuthority> getUserAuthoritiesByUserEmail(String email);

    List<UserInfo> getAllUsers();
}
