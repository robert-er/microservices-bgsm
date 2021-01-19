package com.bgsm.userservice.gui;

import com.bgsm.userservice.gui.forms.CategoryForm;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.service.ItemCategoryService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("category")
public class CategoryView extends VerticalLayout implements HasUrlParameter<String> {

    private String categoryName;

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        categoryName = s;
    }

    @Autowired
    public CategoryView(ItemCategoryService itemCategoryService) {
        MainMenuBar mainMenuBar = new MainMenuBar();
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(mainMenuBar);
        add(menuLayout);

        CategoryForm categoryForm = new CategoryForm(itemCategoryService);
        HorizontalLayout categoryLayout = new HorizontalLayout();
        categoryLayout.add(categoryForm);
        add(categoryLayout);


    }
}
