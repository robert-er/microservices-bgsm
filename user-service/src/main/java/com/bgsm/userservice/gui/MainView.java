package com.bgsm.userservice.gui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route("/")
@Secured("ROLE_ADMIN")
public class MainView extends VerticalLayout {

    public MainView() {
        Button button = new Button("Click me",
                e -> Notification.show("button clicked"));
        add(button);

        Element logoutLink = ElementFactory.createAnchor("logout", "Logout");
        getElement().appendChild(logoutLink);
    }

}
