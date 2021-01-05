package com.bgsm.userservice.dto;

import com.bgsm.userservice.model.ERole;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppUserDto {

    private String username;
    private String password;
    private String email;
    private Set<ERole> roles;
}
