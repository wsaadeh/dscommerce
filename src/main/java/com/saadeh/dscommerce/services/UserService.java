package com.saadeh.dscommerce.services;

import com.saadeh.dscommerce.dto.UserDto;
import com.saadeh.dscommerce.entities.Role;
import com.saadeh.dscommerce.entities.User;
import com.saadeh.dscommerce.projections.UserDetailsProjection;
import com.saadeh.dscommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> listUserDetails = repository.searchUserAndRolesByEmail(username);
        if (listUserDetails.size()==0){
            throw new UsernameNotFoundException("User not found.");
        }

        User user = new User();
        user.setEmail(username);
        user.setPassword(listUserDetails.get(0).getPassword());
        for (UserDetailsProjection u: listUserDetails){
            user.addRole(new Role(u.getRoleId(),u.getAuthority()));
        }

        return user;
    }

    protected User authenticated(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");

            User user = repository.findByEmail(username).get();
            return user;
        }catch (Exception e){
            throw new UsernameNotFoundException("Email not found");
        }

    }

    @Transactional(readOnly = true)
    public UserDto getMe(){
        User user = authenticated();
        return new UserDto(user);
    }
}
