package com.bgsm.userservice.mapper;

import com.bgsm.userservice.dto.AppUserDto;
import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Role;
import com.bgsm.userservice.security.WebSecurityConfig;
import com.bgsm.userservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AppUserMapper {

    private final RoleService roleService;
    private final WebSecurityConfig webSecurityConfig;

    public AppUser mapToAppUser(AppUserDto appUserDto) {
        return AppUser.builder()
                .username(appUserDto.getUsername())
                .password(webSecurityConfig.passwordEncoder().encode(appUserDto.getPassword()))
                .email(appUserDto.getEmail())
                .roles(appUserDto.getRoles().stream()
                        .map(roleService::getRole)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public AppUserDto mapToAppUserDto(AppUser appUser) {
        return AppUserDto.builder()
                .username(appUser.getUsername())
                .password(appUser.getPassword())
                .email(appUser.getEmail())
                .roles(appUser.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toSet()))
                .build();
    }

    public List<AppUserDto> mapToAppUserDtoList(List<AppUser> appUserList) {
        return appUserList.stream()
                .map(this::mapToAppUserDto)
                .collect(Collectors.toList());
    }
}
