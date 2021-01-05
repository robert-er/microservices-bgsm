package com.bgsm.userservice.service;

import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.model.Role;
import com.bgsm.userservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository repository;

    public Role getRole(ERole name) {
        Optional<Role> role = repository.findByName(name);
        return role.orElseGet(() -> repository.save(new Role(name)));
    }
}
