package com.bgsm.userservice.security;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.model.ItemCategory;
import com.bgsm.userservice.repository.AppUserRepository;
import com.bgsm.userservice.repository.ItemCategoryRepository;
import com.bgsm.userservice.security.jwt.AuthEntryPointJwt;
import com.bgsm.userservice.security.jwt.AuthTokenFilter;
import com.bgsm.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final AppUserRepository appUserRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final RoleService roleService;
    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void get() {
        createRoles();
        createPredefinedTestUsers();
        createPredefineItemCategories();
    }

    private void createRoles() {
        roleService.getRole(ERole.ROLE_USER);
        roleService.getRole(ERole.ROLE_ADMIN);
        roleService.getRole(ERole.ROLE_MODERATOR);
    }

    private void createPredefinedTestUsers() {
        if(appUserRepository.findByUsername("user") == null ||
                !appUserRepository.findByUsername("user").isPresent()) {
            AppUser appUser = new AppUser("user", passwordEncoder().encode("user"),
                    "email@user.com", Collections.singleton(roleService.getRole(ERole.ROLE_USER)));
            appUserRepository.save(appUser);
        }

        if(appUserRepository.findByUsername("admin") == null ||
                !appUserRepository.findByUsername("admin").isPresent()) {
            AppUser appAdmin = new AppUser("admin", passwordEncoder().encode("admin"),
                    "email@admin.com", Collections.singleton(roleService.getRole(ERole.ROLE_ADMIN)));
            appUserRepository.save(appAdmin);
        }

        if(appUserRepository.findByUsername("moderator") == null ||
                !appUserRepository.findByUsername("moderator").isPresent()) {
            AppUser appModerator = new AppUser("moderator", passwordEncoder().encode("moderator"),
                    "email@moderator.com", Collections.singleton(roleService.getRole(ERole.ROLE_MODERATOR)));
            appUserRepository.save(appModerator);
        }
    }

    private void createPredefineItemCategories() {
        createItemCategory("rpg");
        createItemCategory("adventure");
        createItemCategory("strategy");
        createItemCategory("card games");
        createItemCategory("educational");
    }

    private void createItemCategory(String name) {
        if(itemCategoryRepository.findByName(name) == null ||
                !itemCategoryRepository.findByName(name).isPresent()) {
            ItemCategory itemCategory = new ItemCategory(name);
            itemCategoryRepository.save(itemCategory);
        }
    }
}
