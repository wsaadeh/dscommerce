package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.entities.User;
import com.saadeh.dscommerce.services.exceptions.ForbiddenException;
import com.saadeh.dscommerce.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    AuthService service;

    @Mock
    UserService userService;

    private User admin,selfClient,otherClient;


    @BeforeEach
    void setUp() throws Exception{
        admin = UserFactory.createAdminUser();
        selfClient = UserFactory.createCustomClientUser(1L,"Bob","bob@gmail.com");
        otherClient = UserFactory.createCustomClientUser(3L,"Ana","ana@gmail.com");
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenAdminLogged(){
        Mockito.when(userService.authenticated()).thenReturn(admin);

        Long userId = admin.getId();

        Assertions.assertDoesNotThrow(()->service.validateSelfOrAdmin(userId));
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfLogged(){
        Mockito.when(userService.authenticated()).thenReturn(selfClient);

        Long userId = selfClient.getId();

        Assertions.assertDoesNotThrow(()->service.validateSelfOrAdmin(userId));

    }

    @Test
    public void validateSelfOrAdminThrowsForbiddenExceptionWhenOtherClientLogged(){
        Mockito.when(userService.authenticated()).thenReturn(selfClient);

        Long userId = otherClient.getId();

        Assertions.assertThrows(ForbiddenException.class,()->service.validateSelfOrAdmin(userId));
    }





}
