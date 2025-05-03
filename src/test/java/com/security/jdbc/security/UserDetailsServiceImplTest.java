package com.security.jdbc.security;

import com.security.jdbc.pojos.UserInfo;
import com.security.jdbc.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl serviceImpl;


    @Test
    void loadUserByUsername() {

        // Arrange
        String email = "maul@email.com";
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setPassword("password");
        userInfo.setEnabled(true);

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        Mockito.when(userRepository.findOneUser(email)).thenReturn(userInfo);
        Mockito.when(userRepository.getUserAuthoritiesByUserEmail(email)).thenReturn(authorities);

        // Act
        UserDetails userDetails = serviceImpl.loadUserByUsername(email);

        // Assert
        assertNotNull(userDetails);
        Assertions.assertEquals( "maul@email.com", userDetails.getUsername());
        Assertions.assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(userDetails.isEnabled());

        //Verify interaction
        Mockito.verify(userRepository, Mockito.times(1)).findOneUser(email);
        Mockito.verify(userRepository, Mockito.times(1)).getUserAuthoritiesByUserEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void shouldThrowException_whenUsernameIsNull() {
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            serviceImpl.loadUserByUsername(null);
        });

        Assertions.assertEquals("Username cannot be null or empty", exception.getMessage());

        //Make sure no interaction happened
        //This is mandatory as we wish no interaction from an error methods
        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    void shouldThrowException_whenUsernameIsEmpty() {
        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            serviceImpl.loadUserByUsername("");
        });

        Assertions.assertEquals("Username cannot be null or empty", exception.getMessage());

        //Make sure no interaction happened
        Mockito.verifyNoInteractions(userRepository);
    }
}