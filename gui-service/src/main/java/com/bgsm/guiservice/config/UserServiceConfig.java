package com.bgsm.guiservice.config;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserServiceConfig {

    @Value("${userservice.api.endpoint}")
    private String userServiceEndpoint;
}
