package com.bgsm.userservice.gui.forms;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MainMenuBar extends FormLayout {

    @Autowired
    public MainMenuBar() {
        HorizontalLayout headerLayout = new HorizontalLayout();
        Html header = new Html("<a href=\"/\" style=\"font-size: 30px\">BOARD GAMES SHARING MARKET</h3>");
        headerLayout.add(header);
        headerLayout.setSizeFull();

        HorizontalLayout menuLayout = new HorizontalLayout();
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(true);

        MenuItem userMenu = menuBar.addItem("User");
        SubMenu userSubMenu = userMenu.getSubMenu();

        Element userItemsView = ElementFactory.createAnchor("useritems", "Your games");
        MenuItem userItemsMenu = userSubMenu.addItem("");
        userItemsMenu.getElement().appendChild(userItemsView);

        Element userOffersView = ElementFactory.createAnchor("useroffers", "Your offers");
        MenuItem userOffersMenu = userSubMenu.addItem("");
        userOffersMenu.getElement().appendChild(userOffersView);

        Element addItem = ElementFactory.createAnchor("additem", "Add new game");
        MenuItem addItemMenu = userSubMenu.addItem("");
        addItemMenu.getElement().appendChild(addItem);

        Element addOffer = ElementFactory.createAnchor("addoffer", "Add new offer");
        MenuItem addOfferMenu = userSubMenu.addItem("");
        addOfferMenu.getElement().appendChild(addOffer);

        MenuItem adminMenu = menuBar.addItem("Administrator");
        SubMenu adminSubMenu = adminMenu.getSubMenu();

        Element categoryLink = ElementFactory.createAnchor("addcategory", "Add new category");
        MenuItem addCategoryMenu = adminSubMenu.addItem("");
        addCategoryMenu.getElement().appendChild(categoryLink);

        Element userLink = ElementFactory.createAnchor("addsuperuser", "Add new superuser");
        MenuItem addSuperUserMenu =   adminSubMenu.addItem("");
        addSuperUserMenu.getElement().appendChild(userLink);

        menuBar.addItem("Search");

        Element signupLink = ElementFactory.createAnchor("signup", "Sign Up");
        signupLink.getStyle().set("text-decoration", "none");
        MenuItem signupMenu = menuBar.addItem("");
        signupMenu.getElement().appendChild(signupLink);

        Element loginLink = ElementFactory.createAnchor("login", "Login");
        loginLink.getStyle().set("text-decoration", "none");
        MenuItem loginMenu = menuBar.addItem("");
        loginMenu.getElement().appendChild(loginLink);

        Element logoutLink = ElementFactory.createAnchor("logout", "Logout");
        logoutLink.getStyle().set("text-decoration", "none");
        MenuItem logoutMenu = menuBar.addItem("");
        logoutMenu.  getElement().appendChild(logoutLink);
        menuBar.setSizeFull();

        menuLayout.add(menuBar);
        menuLayout.setSizeFull();

        VerticalLayout layout = new VerticalLayout();
        layout.add(headerLayout, menuLayout);
        add(layout);
    }
}
