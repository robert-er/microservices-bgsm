package com.bgsm.userservice.gui;

import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("adminpanel")
@Secured("ROLE_ADMIN")
public class AdminPanelView extends VerticalLayout {

    public AdminPanelView() {
        MainMenuBar mainMenuBar = new MainMenuBar();
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(mainMenuBar);
        add(menuLayout);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Label welcomeLabel = new Label("Welcome " + username.toUpperCase() + " in Administration Panel");
        add(welcomeLabel);

        Element categoryLink = ElementFactory.createAnchor("addcategory", "Add new category");
        getElement().appendChild(categoryLink);

        Element userLink = ElementFactory.createAnchor("addsuperuser", "Add new superuser");
        getElement().appendChild(userLink);

        Element mainView = ElementFactory.createAnchor("", "Main Page");
        getElement().appendChild(mainView);
    }
}
