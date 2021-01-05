package com.bgsm.guiservice.dto;

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
