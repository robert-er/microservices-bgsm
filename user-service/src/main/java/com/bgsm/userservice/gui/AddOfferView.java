package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.model.EOfferStatus;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route("addoffer")
@Secured("ROLE_USER")
public class AddOfferView extends VerticalLayout {

    @Autowired
    public AddOfferView(OfferService offerService, OfferMapper offerMapper,
                        ItemService itemService, ItemMapper itemMapper,
                        AppUserService userService) {
        MainMenuBar mainMenuBar = new MainMenuBar();
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(mainMenuBar);
        add(menuLayout);

        OfferDto offerDto = new OfferDto();

        Binder<OfferDto> binder = new Binder<>();
        FormLayout layoutWithBinder = new FormLayout();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long userId = userService.findByName(username).getId();

        Label welcomeLabel = new Label();
        welcomeLabel.setText("Hi " + username.toUpperCase() + ". Add new offer");

        MenuBar menuBarItem = new MenuBar();
        MenuItem item = menuBarItem.addItem("Item");
        Text selectedItem = new Text("");
        List<ItemDto> items = getItems(itemService, itemMapper, userId);

        NumberField selectedItemField = new NumberField();

        Long[] itemId = {0L};
        items.forEach(element -> item.getSubMenu().addItem(element.getName(),
                e -> {selectedItem.setText(element.getName());
                    item.setText(element.getName());
                    selectedItemField.setValue(Double.valueOf(element.getId()));
                    itemId[0] = element.getId();
        }));

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
        bigDecimalField.setPrefixComponent(new Icon(VaadinIcon.EURO));

        bigDecimalField.addValueChangeListener(e -> {
            BigDecimal priceValue;
            if (e.getValue() == null) {
                priceValue = BigDecimal.ZERO;
            } else {
                priceValue = e.getValue().multiply(new BigDecimal("0.23"))
                        .setScale(2, RoundingMode.HALF_EVEN);
            }
            price.setText("VAT 23%: EUR " + priceValue);
        });

        bigDecimalField.setValue(new BigDecimal(0).setScale(2));

        Button save = new Button("Save");
        Button reset = new Button("Reset");

        layoutWithBinder.addFormItem(menuBarItem, "Item");
        layoutWithBinder.addFormItem(dateFrom, "Date from");
        layoutWithBinder.addFormItem(dateTo, "Date to");
        layoutWithBinder.addFormItem(location, "Location");
        layoutWithBinder.addFormItem(bigDecimalField, price);
        HorizontalLayout actions = new HorizontalLayout();
        actions.add(save, reset);
        save.getStyle().set("marginRight", "10px");

        binder.forField(location)
                .withValidator(new StringLengthValidator(
                        "Please add the location", 1, null))
                .bind(OfferDto::getLocation, OfferDto::setLocation);
        binder.forField(dateFrom)
                .withValidator(min -> min.isBefore(dateTo.getValue()) || min.isEqual(dateTo.getValue()),
                        "Start date must be lower or equal end date")
                .bind(OfferDto::getDateFrom, OfferDto::setDateFrom);
        binder.forField(dateTo)
                .withValidator(max -> max.isAfter(dateFrom.getValue()) || max.isEqual(dateFrom.getValue()),
                        "End date must be higher or equal start date")
                .bind(OfferDto::getDateTo, OfferDto::setDateTo);
        binder.forField(selectedItemField)
                .withValidator(num -> num.longValue() > 0L,
                        "Please select item");

        Label infoLabel = new Label();

        save.addClickListener(event -> {
            if (binder.writeBeanIfValid(offerDto)) {
                offerDto.setItemId(itemId[0]);
                offerDto.setStatus(EOfferStatus.ACTIVE);

                if(bigDecimalField.isEmpty()) {
                    offerDto.setPrice(BigDecimal.ZERO);
                } else {
                    offerDto.setPrice(bigDecimalField.getValue());
                }

                OfferDto returnedOfferDto = offerMapper.mapToOfferDto(offerService.save(offerMapper.mapToOffer(offerDto)));
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
        add(welcomeLabel, layoutWithBinder, actions, infoLabel);

        Element mainView = ElementFactory.createAnchor("", "Main Page");
        getElement().appendChild(mainView);
    }

    private List<ItemDto> getItems(ItemService itemService, ItemMapper itemMapper, Long userId) {
        return itemMapper.mapToItemDtoList(itemService.findByUserId(userId));
    }
}
