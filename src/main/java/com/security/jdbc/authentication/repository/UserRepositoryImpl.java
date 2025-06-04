package com.security.jdbc.authentication.repository;


import com.security.jdbc.authentication.mapper.AuthoritiesMapper;
import com.security.jdbc.authentication.mapper.UserInfoMapper;
import com.security.jdbc.authentication.dto.Authorities;
import com.security.jdbc.authentication.dto.UserInfo;
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
public class UserRepositoryImpl implements UserRepository{

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addUser(String email, String password){
        String sql = """ 
        INSERT INTO users (email, password) VALUES (?,?)
        """;
        //This uses "text blocks".
        //refer to this: https://www.baeldung.com/java-text-blocks
        jdbcTemplate.update(sql, email, password);
    }


    @Override
    public UserInfo findOneUserByEnabledAndEmail(String email){
        String sql = """
        SELECT email, password, enabled FROM users WHERE users.enabled = TRUE and users.email = ?
        """;
        try{
            UserInfo userInfo =
                    jdbcTemplate.queryForObject(sql, new UserInfoMapper(), email);
            return userInfo;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    @Override
    public List<GrantedAuthority> getUserAuthoritiesByUserEmail(String email){

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



    @Override
    public List<UserInfo> getAllUsers(){
        logger.info("Get all users method is hit");

            String sql = """
                SELECT * FROM users
                """;
            List<UserInfo> userInfo = this.jdbcTemplate.query(sql, new UserInfoMapper());
            return userInfo;
    }

}
