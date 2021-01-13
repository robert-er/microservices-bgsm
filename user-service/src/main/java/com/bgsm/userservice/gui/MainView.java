package com.bgsm.userservice.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        Button button = new Button("Click me",
                e -> Notification.show("button clicked"));
        add(button);

        Element loginLink = ElementFactory.createAnchor("login", "Login");
        getElement().appendChild(loginLink);

        Element signupLink = ElementFactory.createAnchor("signup", "Sign Up");
        getElement().appendChild(signupLink);

        Element addItem = ElementFactory.createAnchor("additem", "Add new item");
        getElement().appendChild(addItem);

        Element logoutLink = ElementFactory.createAnchor("logout", "Logout");
        getElement().appendChild(logoutLink);
    }
}
