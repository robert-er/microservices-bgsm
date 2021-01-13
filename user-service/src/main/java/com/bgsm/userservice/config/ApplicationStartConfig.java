package com.bgsm.userservice.config;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.model.ItemCategory;
import com.bgsm.userservice.repository.AppUserRepository;
import com.bgsm.userservice.repository.ItemCategoryRepository;
import com.bgsm.userservice.security.WebSecurityConfig;
import com.bgsm.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class ApplicationStartConfig {

    private final AppUserRepository appUserRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final RoleService roleService;

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
            AppUser appUser = new AppUser("user",
                    WebSecurityConfig.passwordEncoder().encode("user"),
                    "email@user.com", Collections.singleton(roleService.getRole(ERole.ROLE_USER)));
            appUserRepository.save(appUser);
        }

        if(appUserRepository.findByUsername("admin") == null ||
                !appUserRepository.findByUsername("admin").isPresent()) {
            AppUser appAdmin = new AppUser("admin",
                    WebSecurityConfig.passwordEncoder().encode("admin"),
                    "email@admin.com", Collections.singleton(roleService.getRole(ERole.ROLE_ADMIN)));
            appUserRepository.save(appAdmin);
        }

        if(appUserRepository.findByUsername("moderator") == null ||
                !appUserRepository.findByUsername("moderator").isPresent()) {
            AppUser appModerator = new AppUser("moderator",
                    WebSecurityConfig.passwordEncoder().encode("moderator"),
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
