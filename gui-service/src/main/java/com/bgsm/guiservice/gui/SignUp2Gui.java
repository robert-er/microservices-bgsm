package com.bgsm.guiservice.gui;

import com.bgsm.guiservice.config.UserServiceConfig;
import com.bgsm.guiservice.dto.AppUserDto;
import com.bgsm.guiservice.dto.ERole;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("add")
public class SignUp2Gui extends VerticalLayout {

    @Autowired
    private UserServiceConfig userServiceConfig;

    @Autowired
    public SignUp2Gui() {
        RestTemplate restTemplate = new RestTemplate();
        FormLayout layoutWithBinder = new FormLayout();
        Binder<AppUserDto> binder = new Binder<>();
        AppUserDto appUserDto = new AppUserDto();

        TextField username = new TextField();
        username.setValueChangeMode(ValueChangeMode.EAGER);
        EmailField email = new EmailField();
        email.setClearButtonVisible(true);
        email.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField password = new PasswordField();
        password.setValueChangeMode(ValueChangeMode.EAGER);

        Label infoLabel = new Label();
        NativeButton save = new NativeButton("Save");
        NativeButton reset = new NativeButton("Reset");

        layoutWithBinder.addFormItem(username, "Username");
        layoutWithBinder.addFormItem(email, "E-mail");
        layoutWithBinder.addFormItem(password, "Password");

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        username.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);

        binder.forField(username)
                .withValidator(new StringLengthValidator(
                        "Please add the user name", 1, null))
                .bind(AppUserDto::getUsername, AppUserDto::setUsername);
        binder.forField(email)
                .withValidator(new EmailValidator("Incorrect email address"))
                .bind(AppUserDto::getEmail, AppUserDto::setEmail);
        binder.forField(password)
                .withValidator(new StringLengthValidator(
                        "Please add the password", 1, null))
                .bind(AppUserDto::getPassword, AppUserDto::setPassword);

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(appUserDto)) {
                infoLabel.setText("User " + appUserDto.getUsername() + " has been created.");
                appUserDto.setRoles(Collections.singleton(ERole.ROLE_USER));
                HttpEntity<AppUserDto> entity = new HttpEntity<>(appUserDto, new HttpHeaders());

                AppUserDto returnedUserDto = restTemplate.exchange(userServiceConfig.getUserServiceEndpoint(),
                        HttpMethod.POST, entity, AppUserDto.class).getBody();
            } else {
                BinderValidationStatus<AppUserDto> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });
        reset.addClickListener(event -> {
            // clear fields by setting null
            binder.readBean(null);
            infoLabel.setText("");
        });
        add(layoutWithBinder, actions, infoLabel);
    }
}
