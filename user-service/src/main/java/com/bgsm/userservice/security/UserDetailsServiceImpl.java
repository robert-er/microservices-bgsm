package com.bgsm.userservice.security;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmailOrUsername(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username or email: " + login));

        return UserDetailsImpl.build(user);
    }


}
