package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.gui.forms.ItemForm;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemCategoryService;
import com.bgsm.userservice.service.ItemService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

import java.util.*;
import java.util.stream.Collectors;

@Route("useritems")
@Secured("ROLE_USER")
public class UserItemsView extends VerticalLayout {

    @Autowired
    public UserItemsView(ItemService itemService, ItemMapper itemMapper, AppUserService userService,
                         ItemCategoryService itemCategoryService) {

        Grid<ItemDto> grid = new Grid<>();
        ItemDto itemDto = new ItemDto();

        Binder<ItemDto> binder = new Binder<>(ItemDto.class);
        List<ItemDto> userItems = itemMapper.mapToItemDtoList(itemService.findByUserId(getCurrentUserId(userService)));
        grid.setItems(userItems);

        Editor<ItemDto> editor = grid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);

        Grid.Column<ItemDto> nameColumn = grid.addColumn(ItemDto::getName)
                .setSortable(true)
                .setHeader("Name");
        Grid.Column<ItemDto> descriptionColumn = grid.addColumn(ItemDto::getDescription)
                .setSortable(true)
                .setHeader("Description");
        Grid.Column<ItemDto> minPlayersColumn = grid.addColumn(e -> (int)e.getMinPlayers()).setHeader("Minimum players")
                .setSortable(true)
                .setHeader("Minimum players");
        Grid.Column<ItemDto> maxPlayersColumn = grid.addColumn(e -> (int)e.getMaxPlayers()).setHeader("Maximum players")
                .setSortable(true)
                .setHeader("Maximum players");
        Grid.Column<ItemDto> categoryColumn = grid.addColumn(ItemDto::getCategoryName)
                .setSortable(true)
                .setHeader("Category");

        Div validationStatus = new Div();
        validationStatus.setId("validation");

        TextField nameField = new TextField();
        TextField descriptionField = new TextField();
        NumberField minPlayersField = new NumberField();
        minPlayersField.setHasControls(true);
        minPlayersField.setMin(1);
        minPlayersField.setMax(100);
        NumberField maxPlayersField = new NumberField();
        maxPlayersField.setHasControls(true);
        maxPlayersField.setMin(1);
        maxPlayersField.setMax(100);
        TextField categoryField = new TextField();

        binder.forField(nameField)
                .withValidator(new StringLengthValidator("Name length must have at least 1 character.", 1, null))
                .withStatusLabel(validationStatus).bind("name");
        nameColumn.setEditorComponent(nameField);
        binder.forField(descriptionField)
                .withValidator(new StringLengthValidator("Description length must have at least 1 character.", 1, null))
                .withStatusLabel(validationStatus).bind("description");
        descriptionColumn.setEditorComponent(descriptionField);
        binder.forField(minPlayersField)
                .withValidator(min -> min <= maxPlayersField.getValue(),
                        "Min players must be lower or equal max players")
                .withStatusLabel(validationStatus).bind("minPlayers");
        minPlayersColumn.setEditorComponent(minPlayersField);
        binder.forField(maxPlayersField)
                .withValidator(max -> max >= minPlayersField.getValue(),
                        "Max players must be higher or equal min players")
                .withStatusLabel(validationStatus).bind("maxPlayers");
        maxPlayersColumn.setEditorComponent(maxPlayersField);
        binder.forField(categoryField)
                .withValidator(new StringLengthValidator("Name length must have at least 1 character.", 1, null))
                .withStatusLabel(validationStatus).bind("categoryName");
        categoryColumn.setEditorComponent(categoryField);

        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());

        Grid.Column<ItemDto> editorColumn = grid.addComponentColumn(edited -> {
            Button edit = new Button("Edit");
            edit.addClassName("edit");
            edit.addClickListener(e -> {
                itemDto.setId(itemService.findByName(edited.getName()).getId());
                itemDto.setName(edited.getName());
                itemDto.setDescription(edited.getDescription());
                itemDto.setMinPlayers(edited.getMinPlayers());
                itemDto.setMaxPlayers(edited.getMaxPlayers());
                itemDto.setCategoryName(edited.getCategoryName());
                itemDto.setUserName(getCurrentUsername());
                editor.editItem(edited);
                nameField.focus();
            });
            edit.setEnabled(!editor.isOpen());
            editButtons.add(edit);
            return edit;
        });

        Grid.Column<ItemDto> deleteColumn = grid.addComponentColumn(edited -> {
            Button delete = new Button("Delete");
            delete.addClassName("delete");
            delete.addClickListener(e -> {
                Long deletedId = itemService.findByName(edited.getName()).getId();
                itemService.deleteById(deletedId);
                grid.getDataCommunicator().getKeyMapper().removeAll();
                grid.setItems(itemMapper.mapToItemDtoList(itemService.findByUserId(getCurrentUserId(userService))));
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
            if (binder.writeBeanIfValid(itemDto)) {
                ItemDto returnedItemDto = itemMapper.mapToItemDto(itemService.save(itemMapper.mapToItem(itemDto)));
            } else {
                BinderValidationStatus<ItemDto> validate = binder.validate();
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
                event -> message.setText(event.getItem().getName() + ", "
                        + event.getItem().getDescription()));

        add(validationStatus, grid);

        ItemForm form = new ItemForm(itemMapper, itemService, itemCategoryService);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(mainContent);
        setSizeFull();
    }

    private void refresh(Grid grid, ItemService itemService, ItemMapper itemMapper, AppUserService userService) {
        List<ItemDto> userItems = itemMapper.mapToItemDtoList(itemService.findByUserId(getCurrentUserId(userService)));
        grid.setItems(userItems);

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
}
