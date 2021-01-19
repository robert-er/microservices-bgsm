package com.bgsm.userservice.gui;

import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        MainMenuBar mainMenuBar = new MainMenuBar();
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(mainMenuBar);
        add(menuLayout);

        Element loginLink = ElementFactory.createAnchor("login", "Login");
        getElement().appendChild(loginLink);

        Element signupLink = ElementFactory.createAnchor("signup", "Sign Up");
        getElement().appendChild(signupLink);

        Element addItem = ElementFactory.createAnchor("additem", "Add new item");
        getElement().appendChild(addItem);

        Element addOffer = ElementFactory.createAnchor("addoffer", "Add new offer");
        getElement().appendChild(addOffer);

        Element adminPanel = ElementFactory.createAnchor("adminpanel", "Administration Panel");
        getElement().appendChild(adminPanel);

        Element logoutLink = ElementFactory.createAnchor("logout", "Logout");
        getElement().appendChild(logoutLink);
    }
}
