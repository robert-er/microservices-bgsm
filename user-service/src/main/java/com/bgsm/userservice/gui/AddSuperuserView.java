package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.AppUserDto;
import com.bgsm.userservice.mapper.AppUserMapper;
import com.bgsm.userservice.model.ERole;
import com.bgsm.userservice.service.AppUserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
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
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route("addsuperuser")
@Secured("ROLE_ADMIN")
public class AddSuperuserView extends VerticalLayout {

    @Autowired
    public AddSuperuserView(AppUserService userService, AppUserMapper userMapper) {
        FormLayout layoutWithBinder = new FormLayout();
        Binder<AppUserDto> binder = new Binder<>();
        AppUserDto appUserDto = new AppUserDto();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String loggedUsername = userDetails.getUsername();

        Label welcomeLabel = new Label();
        welcomeLabel.setText("Hi " + loggedUsername.toUpperCase() + ". Add new user");

        TextField username = new TextField();
        username.setValueChangeMode(ValueChangeMode.EAGER);
        EmailField email = new EmailField();
        email.setClearButtonVisible(true);
        email.setValueChangeMode(ValueChangeMode.EAGER);
        PasswordField password = new PasswordField();
        password.setValueChangeMode(ValueChangeMode.EAGER);

        MenuBar menuBar = new MenuBar();
        MenuItem userRole = menuBar.addItem("Role");
        List<String> roles = getRoles();
        TextField selectedRole = new TextField();

        roles.forEach(role -> userRole.getSubMenu().addItem(role,
                e -> {selectedRole.setValue(role);
                    userRole.setText(role);
                }));

        Label infoLabel = new Label();
        Button save = new Button("Save");
        Button reset = new Button("Reset");

        layoutWithBinder.addFormItem(username, "Username");
        layoutWithBinder.addFormItem(email, "E-mail");
        layoutWithBinder.addFormItem(password, "Password");
        layoutWithBinder.addFormItem(menuBar, "Role");

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
                appUserDto.setRoles(Collections.singleton(ERole.valueOf(selectedRole.getValue())));

                AppUserDto returnedUserDto = userMapper.mapToAppUserDto(userService.save(userMapper.mapToAppUser(appUserDto)));
                infoLabel.setText("User " + returnedUserDto.getUsername() + " has been created.");
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

        Element adminPanelView = ElementFactory.createAnchor("adminpanel", "Administration Panel");
        getElement().appendChild(adminPanelView);

        Element mainView = ElementFactory.createAnchor("", "Main Page");
        getElement().appendChild(mainView);
    }

    private List<String> getRoles() {
        return Stream.of(ERole.values())
                .map(ERole::name)
                .collect(Collectors.toList());

    }
}
