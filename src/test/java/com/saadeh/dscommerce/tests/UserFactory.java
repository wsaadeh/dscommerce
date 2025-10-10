package com.saadeh.dscommerce.tests;

import com.saadeh.dscommerce.dto.UserDto;
import com.saadeh.dscommerce.entities.Role;
import com.saadeh.dscommerce.entities.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class UserFactory {
    public static User createUser() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
        User user = new User(
                1L,
                "Wilson Saadeh ",
                "wilson.saadeh@gmail.com",
                "11984843576",
                LocalDate.parse("16/10/1979", dtf),
                "123456");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;

    }

    public static User createClientUser() {
        User user = new User(1L, "Maria", "maria@gmail.com",
                "988889999", LocalDate.parse("1980-10-21"), "123456");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser() {
        User user = new User(2L, "Alex", "alex@gmail.com",
                "988889999", LocalDate.parse("1981-01-26"), "123456");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomClientUser(Long id, String username, String email) {
        User user = new User(id, username, email,
                "988889999", LocalDate.parse("1980-01-11"), "123456");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String username, String email) {
        User user = new User(id, username, email,
                "988889999", LocalDate.parse("1980-10-03"), "123456");
        user.addRole(new Role(1L, "ROLE_ADMIN"));
        return user;
    }

    public static UserDto createUserDto() {
        return new UserDto(createUser());
    }
}
