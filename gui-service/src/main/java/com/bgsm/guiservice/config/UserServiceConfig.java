package com.bgsm.guiservice.config;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class UserServiceConfig {

    @Value("${userservice.api.endpoint}" + "user")
    private String userServiceEndpoint;

    @Value("${userservice.api.endpoint}" + "user/userdetails/")
    private String userDetailsByUsernameServiceEndpoint;

    @Value("${userservice.api.endpoint}" + "user/byusername")
    private String userByUsernameServiceEndpoint;

    @Value("${userservice.api.endpoint}" + "item")
    private String itemServiceEndpoint;

    @Value("${userservice.api.endpoint}" + "item" + "/useritems")
    private String userItemsServiceEndpoint;

    @Value("${userservice.api.endpoint}" + "itemcategory")
    private String itemCategoryEndpoint;

    @Value("${userservice.api.endpoint}" + "offer")
    private String offerServiceEndpoint;
}
