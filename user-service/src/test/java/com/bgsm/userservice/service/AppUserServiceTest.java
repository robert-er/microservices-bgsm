package com.bgsm.userservice.service;

import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AppUserServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AppUserService userService;

    @Autowired
    private AppUserRepository userRepository;

    @Test
    public void shouldFindById() {
        //Given
        AppUser user = createUser("TomTest");
        Long userId = userService.save(user).getId();
        //When
        AppUser returnedUser = userService.findById(userId);
        //Then
        assertEquals(user, returnedUser);
        //CleanUp
        deleteUser(userId);
    }

    @Test
    public void shouldFindByName() {
        //Given
        String username = "JerryTest";
        AppUser user = createUser(username);
        Long userId = userService.save(user).getId();
        //When
        AppUser returnedUser = userService.findByName(username);
        assertEquals(user, returnedUser);
        //CleanUp
        deleteUser(userId);
    }

    @Test
    public void shouldSave() {
        //Given
        AppUser user = createUser("SaveTest");
        //When
        Long userId = userService.save(user).getId();
        //Then
        assertNotEquals(0L, userId);
        //CleanUp
        deleteUser(userId);
    }

    @Test
    public void shouldDeleteById() {
        //Given
        AppUser user = createUser("DeleteTest");
        Long userId = userService.save(user).getId();
        //When
        deleteUser(userId);
        //Then
        assertEquals(Optional.empty(), userRepository.findById(userId));
    }

    @Test
    public void shouldGetAll() {
        //Given
        AppUser user1 = createUser("User1getAllTest");
        AppUser user2 = createUser("User2getAllTest");
        AppUser user3 = createUser("User3getAllTest");
        Long userId1 = userService.save(user1).getId();
        Long userId2 = userService.save(user2).getId();
        Long userId3 = userService.save(user3).getId();
        //When
        List<AppUser> returnedUsers = userService.getAll();
        //Then
        assertTrue(returnedUsers.contains(user1));
        assertTrue(returnedUsers.contains(user2));
        assertTrue(returnedUsers.contains(user3));
        //CleanUp
        deleteUser(userId1);
        deleteUser(userId2);
        deleteUser(userId3);
    }

    private AppUser createUser(String name) {
        return AppUser.builder()
                .username(name)
                .password("TomPassword123")
                .email("tom@mail.com")
                .roles(Stream.of(ERole.ROLE_USER)
                        .map(roleService::getRole)
                        .collect(Collectors.toSet()))
                .build();
    }

    private void deleteUser(Long userId) {
        userService.deleteById(userId);
    }
}