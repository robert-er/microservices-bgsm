package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.ExistException;
import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;

    public AppUser findById(Long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("user not found, id: " + id));
    }

    public AppUser findByName(String name) throws NotFoundException {
        return repository.findByUsername(name).orElseThrow(() -> new NotFoundException("user not found, name: " + name));
    }

    public AppUser save(AppUser appUser) throws ExistException {
        if(repository.findByUsername(appUser.getUsername()) == null ||
                !repository.findByUsername(appUser.getUsername()).isPresent()) {
           return repository.save(appUser);
        } else {
            throw new ExistException("User name " + appUser.getUsername() + " already registered.");
        }
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<AppUser> getAll() {
        return repository.findAll();
    }
}
