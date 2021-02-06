package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.gui.forms.AddOfferForm;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.model.EOfferStatus;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Route("useroffers")
@Secured("USER")
public class UserOffersView extends VerticalLayout {

    @Autowired
    public UserOffersView(OfferMapper offerMapper, OfferService offerService, AppUserService userService,
                          ItemService itemService, ItemMapper itemMapper) {
        MainMenuBar mainMenuBar = new MainMenuBar();
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(mainMenuBar);
        add(menuLayout);

        Grid<OfferDto> grid = new Grid<>();
        OfferDto offerDto = new OfferDto();

        Binder<OfferDto> binder = new Binder<>(OfferDto.class);
        List<OfferDto> userOffers = offerMapper.mapToOfferDtoList(offerService.findByUserId(getCurrentUserId(userService)));
        grid.setItems(userOffers);

        Editor<OfferDto> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Grid.Column<OfferDto> nameColumn = grid
                .addColumn(offerDto1 -> itemService.findById(offerDto1.getItemId()).getName())
                .setSortable(true)
                .setHeader("Name");

        Grid.Column<OfferDto> dateFromColumn = grid.addColumn(OfferDto::getDateFrom)
                .setSortable(true)
                .setHeader("Date From");
        Grid.Column<OfferDto> dateToColumn = grid.addColumn(OfferDto::getDateTo)
                .setSortable(true)
                .setHeader("Date To");
        Grid.Column<OfferDto> locationColumn = grid.addColumn(OfferDto::getLocation)
                .setSortable(true)
                .setHeader("Location");
        Grid.Column<OfferDto> priceColumn = grid.addColumn(OfferDto::getPrice)
                .setSortable(true)
                .setHeader("Price");
        Grid.Column<OfferDto> statusColumn = grid.addColumn(OfferDto::getStatus)
                .setSortable(true)
                .setHeader("Status");

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField nameField = new TextField();

        DatePicker dateFrom = new DatePicker();
        dateFrom.setValue(LocalDate.now());
        dateFrom.setClearButtonVisible(true);

        DatePicker dateTo = new DatePicker();
        dateTo.setValue(LocalDate.now());
        dateTo.setClearButtonVisible(true);

        TextField location = new TextField();

        BigDecimalField price = new BigDecimalField();

        TextField offerStatus = new TextField();

        MenuBar menuBarItem = new MenuBar();
        MenuItem item = menuBarItem.addItem("Item");
        NumberField selectedItemField = new NumberField();

        Long userId = getCurrentUserId(userService);
        List<ItemDto> items = getItems(itemService, itemMapper, userId);
        Long[] itemId = {0L};
        items.forEach(element -> item.getSubMenu().addItem(element.getName(),
                e -> { item.setText(element.getName());
                    selectedItemField.setValue(Double.valueOf(element.getId()));
                    itemId[0] = element.getId();
                }));

        binder.forField(nameField)
                .withValidator(new StringLengthValidator("Name length must have at least 1 character.", 1, null))
                .bind(offerDto1 -> itemService.findById(offerDto1.getItemId()).getName(),
                        (offerDto1, name) -> itemService.findById(offerDto1.getItemId()).setName(name));
        binder.forField(dateFrom)
                .withValidator(min -> min.isBefore(dateTo.getValue()) || min.isEqual(dateTo.getValue()),
                        "Start date must be lower or equal end date")
                .bind(OfferDto::getDateFrom, OfferDto::setDateFrom);
        dateFromColumn.setEditorComponent(dateFrom);
        binder.forField(dateTo)
                .withValidator(max -> max.isAfter(dateFrom.getValue()) || max.isEqual(dateFrom.getValue()),
                        "End date must be higher or equal start date")
                .bind(OfferDto::getDateTo, OfferDto::setDateTo);
        dateToColumn.setEditorComponent(dateTo);
        binder.forField(location)
                .withValidator(new StringLengthValidator(
                        "Please add the location", 1, null))
                .bind(OfferDto::getLocation, OfferDto::setLocation);
        locationColumn.setEditorComponent(location);
        binder.forField(price)
                .bind(OfferDto::getPrice, OfferDto::setPrice);
        priceColumn.setEditorComponent(price);
        binder.forField(offerStatus)
                .bind(offerDto1 -> offerDto1.getStatus().toString(),
                        (offerDto2, status1) -> offerDto2.setStatus(EOfferStatus.valueOf(status1)));
        statusColumn.setEditorComponent(offerStatus);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<OfferDto> editorColumn = grid.addComponentColumn(edited -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                offerDto.setId(edited.getId());
                offerDto.setItemId(edited.getItemId());
                offerDto.setDateFrom(edited.getDateFrom());
                offerDto.setDateTo(edited.getDateTo());
                offerDto.setLocation(edited.getLocation());
                offerDto.setPrice(edited.getPrice());
                offerDto.setStatus(edited.getStatus());
                editor.editItem(edited);
                nameField.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        Grid.Column<OfferDto> deleteColumn = grid.addComponentColumn(edited -> {
            Button delete = new Button("Delete");
            delete.addClassName("delete");
            delete.addClickListener(e -> {
                Long deletedId = offerService.findByItem(itemService.findById(edited.getItemId())).getId();
                offerService.deleteById(deletedId);
                grid.getDataCommunicator().getKeyMapper().removeAll();
                grid.setItems(offerMapper.mapToOfferDtoList(offerService.findByUserId(getCurrentUserId(userService))));
            });
            delete.setEnabled(!editor.isOpen());
            editButtons.add(delete);

            return delete;
        });

        editor.addOpenListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));
        editor.addCloseListener(e -> editButtons.stream()
                .forEach(button -> button.setEnabled(!editor.isOpen())));

        Button save = new Button("Save", e -> {
            if (binder.writeBeanIfValid(offerDto)) {
                OfferDto returnedOfferDto = offerMapper.mapToOfferDto(offerService.save(offerMapper.mapToOffer(offerDto)));
            } else {
                BinderValidationStatus<OfferDto> validate = binder.validate();
                String errorText = validate.getFieldValidationStatuses()
                        .stream().filter(BindingValidationStatus::isError)
                        .map(BindingValidationStatus::getMessage)
                        .map(Optional::get).distinct()
                        .collect(Collectors.joining(", "));
            }
            editor.save();
        });
        save.addClassName("save");

        Button cancel = new Button("Cancel", e -> editor.cancel());
        cancel.addClassName("cancel");

        grid.getElement().addEventListener("keyup", event -> editor.cancel())
                .setFilter("event.key === 'Escape' || event.key === 'Esc'");

        Div buttons = new Div(save, cancel);
        editorColumn.setEditorComponent(buttons);

        Label message = new Label();

        editor.addSaveListener(
                event -> message.setText(event.getItem().getId() + ", "
                        + event.getItem().getItemId()));

        add(validationStatus, grid);

        AddOfferForm form = new AddOfferForm(itemService, itemMapper, userId, offerMapper, offerService);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(mainContent);
        setSizeFull();
    }

    private Long getCurrentUserId(AppUserService userService) {
        String username = getCurrentUsername();
        return userService.findByName(username).getId();
    }

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private List<ItemDto> getItems(ItemService itemService, ItemMapper itemMapper, Long userId) {
        return itemMapper.mapToItemDtoList(itemService.findByUserId(userId));
    }
}
