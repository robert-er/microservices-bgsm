package com.bgsm.guiservice.gui;

import com.bgsm.guiservice.config.UserServiceConfig;
import com.bgsm.guiservice.dto.ItemDto;
import com.bgsm.guiservice.dto.OfferDto;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Route("addoffer")
public class AddOfferGui extends VerticalLayout {

    @Autowired
    private final UserServiceConfig userServiceConfig;

    @Autowired
    public AddOfferGui(UserServiceConfig userServiceConfig) {
        this.userServiceConfig = userServiceConfig;

        RestTemplate restTemplate = new RestTemplate();
        OfferDto offerDto = new OfferDto();

        Binder<OfferDto> binder = new Binder<>();
        FormLayout layoutWithBinder = new FormLayout();

        MenuBar menuBarItem = new MenuBar();
        MenuItem item = menuBarItem.addItem("Item");
        Text selectedItem = new Text("");
        List<ItemDto> items = getItems();

        AtomicReference<Long> itemId = new AtomicReference<>(0L);
        items.forEach(element -> item.getSubMenu().addItem(String.valueOf(element),
                e -> {selectedItem.setText(element.getName());
                    item.setText(element.getName());
                    itemId.set(element.getId());
        }));

        Div message = new Div(new Text("Selected: "), selectedItem);

        DatePicker dateFrom = new DatePicker();
        dateFrom.setValue(LocalDate.now());
        dateFrom.setClearButtonVisible(true);

        DatePicker dateTo = new DatePicker();
        dateTo.setValue(LocalDate.now());
        dateTo.setClearButtonVisible(true);

        TextField location = new TextField();

        Paragraph price = new Paragraph();

        BigDecimalField bigDecimalField = new BigDecimalField("Total cost");
        bigDecimalField.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        bigDecimalField.setPrefixComponent(new Icon(VaadinIcon.DOLLAR));

        bigDecimalField.addValueChangeListener(e -> {
            BigDecimal priceValue;
            if (e.getValue() == null) {
                priceValue = BigDecimal.ZERO;
            } else {
                priceValue = e.getValue().multiply(new BigDecimal("0.24"))
                        .setScale(2, RoundingMode.HALF_EVEN);
            }
            price.setText("VAT 23%: $" + priceValue);
        });

        bigDecimalField.setValue(new BigDecimal(15).setScale(2));

        Button save = new Button("Save");
        Button reset = new Button("Reset");

        layoutWithBinder.addFormItem(menuBarItem, "Item");
        layoutWithBinder.addFormItem(dateFrom, "Date from");
        layoutWithBinder.addFormItem(dateTo, "Date to");
        layoutWithBinder.addFormItem(location, "Location");
        layoutWithBinder.addFormItem(price, "Price");
     //   add(bigDecimalField, price);
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

        binder.forField(location)
                .withValidator(new StringLengthValidator(
                        "Please add the location", 1, null))
                .bind(OfferDto::getLocation, OfferDto::setLocation);
        binder.forField(dateFrom)
                .withValidator(min -> min.isBefore(dateTo.getValue()),
                        "Date from must be lower or equal max players")
                .bind(OfferDto::getDateFrom, OfferDto::setDateFrom);
        binder.forField(dateTo)
                .withValidator(max -> max.isAfter(dateFrom.getValue()),
                        "Date to must be higher or equal min players")
                .bind(OfferDto::getDateTo, OfferDto::setDateTo);

        Label infoLabel = new Label();

        String finalUsername = username;
        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(offerDto)) {
                offerDto.setItemId(itemId.get());

                HttpEntity<OfferDto> entity = new HttpEntity<>(offerDto, new HttpHeaders());
                OfferDto returnedOfferDto = restTemplate.exchange(userServiceConfig.getItemServiceEndpoint(),
                        HttpMethod.POST, entity, OfferDto.class).getBody();
                infoLabel.setText("Offer Item id:  " + returnedOfferDto.getItemId() + " has been created.");
            } else {
                BinderValidationStatus<OfferDto> validate = binder.validate();
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

    private List<ItemDto> getItems() {
        RestTemplate restTemplate = new RestTemplate();
        String userItemsApi = userServiceConfig.getUserItemsServiceEndpoint();
        ItemDto[] userItemsResponse = restTemplate.getForObject(userItemsApi, ItemDto[].class);
        return Optional.ofNullable(userItemsResponse).map(Arrays::asList).orElseGet(ArrayList::new);
    }
}
