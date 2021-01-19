package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.ItemCategoryDto;
import com.bgsm.userservice.mapper.ItemCategoryMapper;
import com.bgsm.userservice.service.ItemCategoryService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import java.util.Optional;
import java.util.stream.Collectors;

@Route("addcategory")
@Secured("ROLE_ADMIN")
public class AddCategoryView extends VerticalLayout {

    @Autowired
    public AddCategoryView(ItemCategoryService itemCategoryService, ItemCategoryMapper itemCategoryMapper) {
        ItemCategoryDto itemCategoryDto = new ItemCategoryDto();

        Binder<ItemCategoryDto> binder = new Binder<>();
        FormLayout layoutWithBinder = new FormLayout();

        TextField name = new TextField();

        MenuBar menuBar = new MenuBar();
        MenuItem category = menuBar.addItem("Category");
        Text selectedCategory = new Text("");

        Button save = new Button("Save");
        Button reset = new Button("Reset");

        layoutWithBinder.addFormItem(name, "Category name");

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Please add the category name", 1, null))
                .bind(ItemCategoryDto::getName, ItemCategoryDto::setName);

        Label infoLabel = new Label();

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(itemCategoryDto)) {
                ItemCategoryDto returnedItemCategoryDto = itemCategoryMapper
                        .mapToItemCategoryDto(itemCategoryService.save(itemCategoryMapper.mapToItemCategory(itemCategoryDto)));
                infoLabel.setText("Category:  " + returnedItemCategoryDto.getName() + " has been created.");
            } else {
                BinderValidationStatus<ItemCategoryDto> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
                infoLabel.setText("There are errors: " + errorText);
            }
        });
        reset.addClickListener(event -> {
            binder.readBean(null);
            infoLabel.setText("");
        });
        add(layoutWithBinder, actions, infoLabel);

        Element adminPanelView = ElementFactory.createAnchor("adminpanel", "Administration Panel");
        getElement().appendChild(adminPanelView);

        Element mainView = ElementFactory.createAnchor("", "Main Page");
        getElement().appendChild(mainView);
    }
}
