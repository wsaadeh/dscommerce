package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.UserDto;
import com.saadeh.dscommerce.entities.Role;
import com.saadeh.dscommerce.entities.User;
import com.saadeh.dscommerce.projections.UserDetailsProjection;
import com.saadeh.dscommerce.repositories.UserRepository;
import com.saadeh.dscommerce.tests.UserDetailsFactory;
import com.saadeh.dscommerce.tests.UserFactory;
import com.saadeh.dscommerce.util.CustomUserUtil;
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
import org.springframework.util.Assert;

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
    private String userName;
    private String nonUserName;

    @Mock
    private CustomUserUtil customUserUtil;

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
        Mockito.when(repository.findByEmail(nonUserName)).thenReturn(Optional.empty());
//        Mockito.doThrow(UsernameNotFoundException.class).when(repository).findByEmail(nonUserName);

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
        Mockito.when(customUserUtil.getLoggedUserName()).thenReturn(userName);

        Assertions.assertDoesNotThrow(()->service.authenticated());
    }

    @Test
    public void authenticatedShouldReturnUserWhenUserExists(){
        Mockito.when(customUserUtil.getLoggedUserName()).thenReturn(userName);

        User result = service.authenticated();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getUsername(),userName);
    }

    @Test
    public void authenticatedShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist(){

        Mockito.when(customUserUtil.getLoggedUserName()).thenReturn(nonUserName);

        Assertions.assertThrows(UsernameNotFoundException.class,()->{
           service.authenticated();
        });
    }

    @Test
    public void authenticatedShouldThrowClassCastExceptionWhenUserDoesNotExist(){
        Mockito.doThrow(ClassCastException.class).when(customUserUtil).getLoggedUserName();

        Assertions.assertThrows(UsernameNotFoundException.class,()->{
            service.authenticated();
        });
    }

    @Test
    public void getMeShouldReturnDTOWhenUserAuthenticated() {
        Mockito.when(customUserUtil.getLoggedUserName()).thenReturn(userName);

        dto = service.getMe();

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getEmail(),userName);
    }

    @Test
    public void getMeShouldReturnDTOWhenUserAuthenticatedUsingSpy() {
        UserService spyUserService = Mockito.spy(service);
        Mockito.doReturn(user).when(spyUserService).authenticated();

        UserDto result = spyUserService.getMe();

        Assertions.assertNotNull(dto);
        Assertions.assertEquals(dto.getEmail(),userName);

    }

    @Test
    public void getMeShouldThrowUsernameNotFoundExceptionWhenUserNotAuthenticated(){
        UserService spyUserService = Mockito.spy(service);
        Mockito.doThrow(UsernameNotFoundException.class).when(spyUserService).authenticated();

        Assertions.assertThrows(UsernameNotFoundException.class,()->{
            UserDto result = spyUserService.getMe();
        });
    }



}
