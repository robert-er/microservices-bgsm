package com.bgsm.userservice.controller;

import com.bgsm.userservice.dto.AppUserDto;
import com.bgsm.userservice.mapper.AppUserMapper;
import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.Role;
import com.bgsm.userservice.security.WebSecurityConfig;
import com.bgsm.userservice.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final AppUserService userService;
    private final AppUserMapper userMapper;
    private final WebSecurityConfig webSecurityConfig;


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public AppUserDto getUser(@PathVariable Long id) {
        return userMapper.mapToAppUserDto(userService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<AppUserDto> getAll() {
        return userMapper.mapToAppUserDtoList(userService.getAll());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public AppUserDto updateUser(@PathVariable Long id, @RequestBody AppUserDto userDto) {
        AppUser user = userService.findById(id);
        user.setUsername(userDto.getUsername());
        user.setPassword(webSecurityConfig.passwordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(userDto.getRoles().stream()
                .map(Role::new)
                .collect(Collectors.toSet()));
        return userMapper.mapToAppUserDto(userService.save(user));
    }
}
