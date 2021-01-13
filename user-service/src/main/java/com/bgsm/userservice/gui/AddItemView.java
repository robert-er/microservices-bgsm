package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.service.ItemCategoryService;
import com.bgsm.userservice.service.ItemService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("additem")
@Secured("ROLE_USER")
public class AddItemView extends VerticalLayout {

    @Autowired
    public AddItemView(ItemService itemService, ItemMapper itemMapper, ItemCategoryService itemCategoryService) {
        ItemDto itemDto = new ItemDto();

        Binder<ItemDto> binder = new Binder<>();
        FormLayout layoutWithBinder = new FormLayout();

        TextField name = new TextField();
        TextField description = new TextField();
        NumberField minPlayers = new NumberField();
        minPlayers.setValue(1d);
        minPlayers.setHasControls(true);
        minPlayers.setMin(1);
        minPlayers.setMax(100);

        NumberField maxPlayers = new NumberField();
        maxPlayers.setValue(1d);
        maxPlayers.setHasControls(true);
        maxPlayers.setMin(1);
        maxPlayers.setMax(100);

        MenuBar menuBar = new MenuBar();
        MenuItem category = menuBar.addItem("Category");

        TextField selectedCategoryField = new TextField();

        Button save = new Button("Save");
        Button reset = new Button("Reset");

        layoutWithBinder.addFormItem(name, "Name");
        layoutWithBinder.addFormItem(description, "Description");
        layoutWithBinder.addFormItem(minPlayers, "Min players");
        layoutWithBinder.addFormItem(maxPlayers, "Max players");
        layoutWithBinder.addFormItem(menuBar, "Category");

        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        List<String> categories = getCategoryList(itemCategoryService);

        categories.forEach(element -> category.getSubMenu().addItem(element,
                        e -> {selectedCategoryField.setValue(element);
                                category.setText(element);}));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Label welcomeLabel = new Label();
        welcomeLabel.setText("Hi " + username.toUpperCase() + ". Add new item");
        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Please add the name", 1, null))
                .bind(ItemDto::getName, ItemDto::setName);
        binder.forField(description)
                .withValidator(new StringLengthValidator(
                        "Please add the description", 1, null))
                .bind(ItemDto::getDescription, ItemDto::setDescription);
        binder.forField(minPlayers)
                .withValidator(min -> min <= maxPlayers.getValue(),
                        "Min players must be lower or equal max players")
                .bind(ItemDto::getMinPlayers, ItemDto::setMinPlayers);
        binder.forField(maxPlayers)
                .withValidator(max -> max >= minPlayers.getValue(),
                        "Max players must be higher or equal min players")
                .bind(ItemDto::getMaxPlayers, ItemDto::setMaxPlayers);
        binder.forField(selectedCategoryField)
                .withValidator(new StringLengthValidator(
                        "Please select category", 1, null))
                .bind(ItemDto::getCategoryName, ItemDto::setCategoryName);

        Label infoLabel = new Label();

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(itemDto)) {
                itemDto.setUserName(username);

                ItemDto returnedItemDto = itemMapper.mapToItemDto(itemService.save(itemMapper.mapToItem(itemDto)));
                infoLabel.setText("Item:  " + returnedItemDto.getName() + " has been created.");
            } else {
                BinderValidationStatus<ItemDto> validate = binder.validate();
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
        add(welcomeLabel, layoutWithBinder, actions, infoLabel);

        Element mainView = ElementFactory.createAnchor("", "Main Page");
        getElement().appendChild(mainView);
    }

    private List<String> getCategoryList(ItemCategoryService itemCategoryService) {
        List<String> categoryResponse = itemCategoryService.getCategoryNames();
        return Optional.ofNullable(categoryResponse).orElseGet(ArrayList::new);
    }
}
