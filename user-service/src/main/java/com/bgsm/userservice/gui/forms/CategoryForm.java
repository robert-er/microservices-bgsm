package com.bgsm.userservice.gui.forms;

import com.bgsm.userservice.service.ItemCategoryService;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryForm extends FormLayout {

    @Autowired
    public CategoryForm(ItemCategoryService itemCategoryService) {
        List<String> categories = getCategoryList(itemCategoryService);

        for(String category : categories) {
            Element element = ElementFactory.createAnchor("category/" + category, category);
            getElement().appendChild(element);
        }
    }

    private List<String> getCategoryList(ItemCategoryService itemCategoryService) {
        List<String> categoryResponse = itemCategoryService.getCategoryNames();
        return Optional.ofNullable(categoryResponse).orElseGet(ArrayList::new);
    }
}
