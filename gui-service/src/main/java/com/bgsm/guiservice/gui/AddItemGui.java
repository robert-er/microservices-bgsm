package com.bgsm.guiservice.gui;

import com.bgsm.guiservice.config.UserServiceConfig;
import com.bgsm.guiservice.dto.ItemDto;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
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

import java.util.*;
import java.util.stream.Collectors;

@Route("additem")
public class AddItemGui extends VerticalLayout {

    @Autowired
    private final UserServiceConfig userServiceConfig;

    @Autowired
    public AddItemGui(UserServiceConfig userServiceConfig) {
        this.userServiceConfig = userServiceConfig;
        RestTemplate restTemplate = new RestTemplate();
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
        Text selectedCategory = new Text("");
        Div message = new Div(new Text("Selected: "), selectedCategory);

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

        List<String> categories = getCategoryList();

        categories.forEach(element -> category.getSubMenu().addItem(element,
                        e -> {selectedCategory.setText(element);
                                category.setText(element);}));

        VaadinSession session = VaadinSession.getCurrent();
        String username = "";
        try {
            username = session.getAttribute("username").toString();
        } catch(Exception e) {
            // no username is session
        }

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

        Label infoLabel = new Label();

        String finalUsername = username;
        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(itemDto)) {
                itemDto.setCategoryName(selectedCategory.getText());
                itemDto.setUserName(finalUsername);

                HttpEntity<ItemDto> entity = new HttpEntity<>(itemDto, new HttpHeaders());
                ItemDto returnedItemDto = restTemplate.exchange(userServiceConfig.getItemServiceEndpoint(),
                        HttpMethod.POST, entity, ItemDto.class).getBody();
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
        add(layoutWithBinder, actions, infoLabel);
    }

    public List<String> getCategoryList() {
        RestTemplate restTemplate = new RestTemplate();
            String categoryApi = userServiceConfig.getItemCategoryEndpoint();
            String[] categoryResponse = restTemplate.getForObject(categoryApi, String[].class);
            return Optional.ofNullable(categoryResponse).map(Arrays::asList).orElseGet(ArrayList::new);

    }
}
