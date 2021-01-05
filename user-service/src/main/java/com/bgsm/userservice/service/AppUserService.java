package com.bgsm.userservice.service;

import com.bgsm.userservice.exception.NotFoundException;
import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;

    public AppUser findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("user not found, id: " + id));
    }

    public AppUser save(AppUser appUser) {
        return repository.save(appUser);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public List<AppUser> getAll() {
        return repository.findAll();
    }
}
