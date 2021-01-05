package com.bgsm.guiservice.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("adduser")
public class SignUpGui extends VerticalLayout {

    @Autowired
//    public SignUpGui(AppUserService appUserService, AppUserMapper appUserMapper) {

    public SignUpGui() {
        VerticalLayout layout = new VerticalLayout();

        TextField usernameField = new TextField();
        usernameField.setLabel("Username");
        usernameField.setPlaceholder("your username");

        TextField passwordField = new TextField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("your password");

        TextField emailField = new TextField();
        emailField.setLabel("Email");
        emailField.setPlaceholder("your email");

        Button button = new Button("save");
        button.setMaxWidth("25em");

        button.addClickListener(buttonClickEvent -> {
//            AppUserDto appUserDto = new AppUserDto(usernameField.getValue(),
//                    passwordField.getValue(),
//                    emailField.getValue(),
//                    Collections.singleton(ERole.ROLE_USER));
//            appUserService.save(appUserMapper.mapToAppUser(appUserDto));
        });

        layout.add(usernameField,emailField, passwordField, button);
        add(layout);
    }
}
