package com.bgsm.guiservice.security.service;

import com.bgsm.guiservice.config.UserServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceConfig userServiceConfig;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        User user = findUserByUsername(username);
//
//        User.UserBuilder builder = null;
//        if (user != null) {
//            builder = org.springframework.security.core.userdetails.User.withUsername(username);
//            builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
//            builder.roles(String.valueOf(user.getAuthorities()));
//        } else {
//            throw new UsernameNotFoundException("User not found.");
//        }
//
//        return builder.build();
        //-----------------------------------------------------
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<UserDetails> entity = new HttpEntity<>(null, new HttpHeaders());
//        UserDetails returnedUser = restTemplate.exchange(userServiceConfig.getUserDetailsByUsernameServiceEndpoint() + username,
//                HttpMethod.GET, entity, UserDetails.class).getBody();
//        UserDetailsImpl userDetails = UserDetailsImpl.build(returnedUser);
//
//        System.out.println("findUserByUsername - user auth: " + userDetails.getAuthorities());
//        return  userDetails;

        return null;
    }


    private UserDetailsImpl findUserByUsername(String username) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserDetails> entity = new HttpEntity<>(null, new HttpHeaders());
        UserDetails returnedUser = restTemplate.exchange(userServiceConfig.getUserDetailsByUsernameServiceEndpoint() + username,
                HttpMethod.GET, entity, UserDetails.class).getBody();
//
//        User user =  new User(username, returnedUser.getPassword(), returnedUser.getAuthorities());
//        System.out.println("findUserByUsername - user auth: " + user.getAuthorities());
//        return user;

            UserDetailsImpl userDetails = UserDetailsImpl.build(returnedUser);

        System.out.println("findUserByUsername - user auth: " + userDetails.getAuthorities());
            return  userDetails;
    }
}
