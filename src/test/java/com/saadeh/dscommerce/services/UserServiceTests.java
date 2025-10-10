package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.UserDto;
import com.saadeh.dscommerce.entities.Role;
import com.saadeh.dscommerce.entities.User;
import com.saadeh.dscommerce.projections.UserDetailsProjection;
import com.saadeh.dscommerce.repositories.UserRepository;
import com.saadeh.dscommerce.tests.UserDetailsFactory;
import com.saadeh.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    private User user;
    private UserDto dto;
    private UserDetailsProjection userDetails;
    private List<UserDetailsProjection> listUserDetails = new ArrayList<>();
    private List<UserDetailsProjection> listUserDetailsEmpty = new ArrayList<>();
    private String userName;
    private String nonUserName;

    @Mock
    private Jwt jwtPrincipal;

    @BeforeEach
    void setUp() throws Exception {
        user = UserFactory.createUser();
        dto = UserFactory.createUserDto();
        userName = user.getEmail();
        nonUserName = "ana@gmail.com";
        listUserDetails = UserDetailsFactory.createCustomAdminUser(userName);

        Mockito.when(repository.searchUserAndRolesByEmail(userName)).thenReturn(listUserDetails);
        Mockito.when(repository.searchUserAndRolesByEmail(nonUserName)).thenReturn(new ArrayList<>());
        
        Mockito.when(repository.findByEmail(userName)).thenReturn(Optional.of(user));

        Mockito.when(jwtPrincipal.getClaim("username")).thenReturn(userName);

    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailWhenUserExist() {
        UserDetails usrDetails = service.loadUserByUsername(userName);

        Assertions.assertNotNull(usrDetails);
        Assertions.assertEquals(usrDetails.getUsername(), user.getEmail());
        Assertions.assertEquals(usrDetails.getPassword(), user.getPassword());
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername(nonUserName);
        });
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserIsLogged() {
        Assertions.assertDoesNotThrow(()->service.authenticated());
    }

    @Test
    public void getMeShould() {

    }


}
