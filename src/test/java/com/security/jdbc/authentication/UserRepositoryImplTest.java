package com.security.jdbc.authentication;

import com.security.jdbc.authentication.repository.UserRepositoryImpl;
import com.security.jdbc.authentication.mapper.AuthoritiesMapper;
import com.security.jdbc.authentication.mapper.UserInfoMapper;
import com.security.jdbc.authentication.dto.Authorities;
import com.security.jdbc.authentication.dto.UserInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {


    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    void addUser() {
        // Arrange
        String email = "test@example.com";
        String password = "securePassword";

        // Act
        userRepositoryImpl.addUser(email, password);

        // Assert
        Mockito.verify(jdbcTemplate, times(1)).update(
                anyString(),
                eq(email),
                eq(password)
        );
    }


    @Test
    void findOneUserByEnabledAndEmail() {
        String email = "john@email.com";

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword("hashedPassword");
        userInfo.setEnabled(true);

        String sql = """
        SELECT email, password, enabled FROM users WHERE users.enabled = TRUE and users.email = ?
        """;
        Mockito.when(
                jdbcTemplate.queryForObject(
                        //anyString(), it works
                        Mockito.eq(sql), // make this strict
                        Mockito.any(UserInfoMapper.class),
                        Mockito.eq(email)
                        )).thenReturn(userInfo);

        UserInfo result = userRepositoryImpl.findOneUserByEnabledAndEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(email, result.getEmail());
        assertEquals("hashedPassword", result.getPassword());
        assertTrue(result.isEnabled());

        Mockito.verify(jdbcTemplate, Mockito.times(1))
                .queryForObject(eq(sql), any(UserInfoMapper.class), eq(email));
    }

    @Test
    void findOneUser_shouldReturnNull_whenUserByEnabledAndEmailDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";

        Mockito.when(jdbcTemplate.queryForObject(
                anyString(),
                any(UserInfoMapper.class),
                eq(email)
        )).thenThrow(new EmptyResultDataAccessException(1));

        // Act
        UserInfo result = userRepositoryImpl.findOneUserByEnabledAndEmail(email);

        // Assert
        assertNull(result);

    }

    @Test
    void getUserAuthoritiesByUserEmail() {
        // Arrange
        String email = "test@example.com";

        Authorities auth1 = new Authorities();
        auth1.setAuthority("ROLE_USER");

        Authorities auth2 = new Authorities();
        auth2.setAuthority("ROLE_ADMIN");

        List<Authorities> mockAuthorities = Arrays.asList(auth1, auth2);

        Mockito.when(jdbcTemplate.query(
                anyString(),
                any(AuthoritiesMapper.class),
                eq(email)
        )).thenReturn(mockAuthorities);

        // Act
        List<GrantedAuthority> result = userRepositoryImpl.getUserAuthoritiesByUserEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(result.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));


        Mockito.verify(jdbcTemplate, Mockito.times(1)).query(
                anyString(),
                any(AuthoritiesMapper.class),
                eq(email)
        );
    }

    @Test
    void getAllUsers_shouldReturnListOfUserInfo_whenQuerySuccessful() {
        // Arrange
        UserInfo user1 = new UserInfo();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setEnabled(true);

        UserInfo user2 = new UserInfo();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setEnabled(false);

        List<UserInfo> mockUsers = Arrays.asList(user1, user2);

        Mockito.when(jdbcTemplate.query(
                anyString(),
                any(UserInfoMapper.class)
        )).thenReturn(mockUsers);

        // Act
        List<UserInfo> result = userRepositoryImpl.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1@example.com", result.get(0).getEmail());
        assertEquals("user2@example.com", result.get(1).getEmail());


        Mockito.verify(jdbcTemplate, times(1)).query(anyString(), any(UserInfoMapper.class));
    }


}