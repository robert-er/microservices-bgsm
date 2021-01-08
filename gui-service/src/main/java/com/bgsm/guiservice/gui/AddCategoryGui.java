package com.bgsm.guiservice.gui;

import com.bgsm.guiservice.config.UserServiceConfig;
import com.bgsm.guiservice.dto.ItemCategoryDto;
import com.bgsm.guiservice.dto.ItemDto;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.stream.Collectors;

@Route("addcategory")
public class AddCategoryGui extends VerticalLayout {

    @Autowired
    private final UserServiceConfig userServiceConfig;

    @Autowired
    public AddCategoryGui(UserServiceConfig userServiceConfig) {
        this.userServiceConfig = userServiceConfig;
        RestTemplate restTemplate = new RestTemplate();
        ItemDto itemDto = new ItemDto();

        Binder<ItemDto> binder = new Binder<>();
        FormLayout layoutWithBinder = new FormLayout();

        TextField name = new TextField();

        MenuBar menuBar = new MenuBar();
        MenuItem category = menuBar.addItem("Category");
        Text selectedCategory = new Text("");
        Div message = new Div(new Text("Selected: "), selectedCategory);

        Button save = new Button("Save");
        Button reset = new Button("Reset");

        layoutWithBinder.addFormItem(name, "Category name");


        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        VaadinSession session = VaadinSession.getCurrent();
        String username = "";
        try {
            username = session.getAttribute("username").toString();
        } catch(Exception e) {
            // no username is session
        }

        binder.forField(name)
                .withValidator(new StringLengthValidator(
                        "Please add the category name", 1, null))
                .bind(ItemDto::getName, ItemDto::setName);

        Label infoLabel = new Label();

        String finalUsername = username;
        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(itemDto)) {

                HttpEntity<ItemDto> entity = new HttpEntity<>(itemDto, new HttpHeaders());
                ItemCategoryDto returnedItemCategoryDto = restTemplate.exchange(userServiceConfig.getItemCategoryEndpoint(),
                        HttpMethod.POST, entity, ItemCategoryDto.class).getBody();
                infoLabel.setText("Category:  " + returnedItemCategoryDto.getName() + " has been created.");
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
        add(layoutWithBinder, actions, infoLabel);

    }

}
