package com.security.jdbc.repository;


import com.security.jdbc.mapper.AuthoritiesMapper;
import com.security.jdbc.mapper.UserInfoMapper;
import com.security.jdbc.pojos.Authorities;
import com.security.jdbc.pojos.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addUser(String email, String password){
        String sql = """ 
        INSERT INTO users (email, password) VALUES (?,?)
        """;
        //This uses "text blocks".
        //refer to this: https://www.baeldung.com/java-text-blocks
        jdbcTemplate.update(sql, email, password);
    }


    public UserInfo findOneUser(String email){
        String sql = """
        SELECT email, password, enabled FROM users WHERE users.enabled = 1 and users.email = ?
        """;
        try{
            UserInfo userInfo =
                    jdbcTemplate.queryForObject(sql, new UserInfoMapper(), email);
            return userInfo;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    public List<GrantedAuthority> getUserAuthoritiesByUserEmail(String email) {
        String authoritiesQuery = """
        SELECT a.authorities_name as authority
        FROM user_authorities ua
        JOIN users u on ua.user_id = u.id
        JOIN authorities a on ua.authorities_id = a.id
        WHERE u.email = ?
        """;

        List<Authorities> authorities = jdbcTemplate.query(authoritiesQuery, new AuthoritiesMapper(), email);

        logger.info("Retrieved authorities: {}", authorities);
        logger.info("The size of authorities: {}", authorities.size());

        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
    }

}
