package com.bgsm.guiservice.gui;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route
@Secured("ROLE_ADMIN")
public class AdminView extends VerticalLayout {

    public AdminView() {
        Label label = new Label("Looks like you are admin!");
        add(label);
    }

}
