package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.ItemDto;
import com.bgsm.userservice.gui.forms.ItemForm;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.ItemCategoryService;
import com.bgsm.userservice.service.ItemService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Route("useritems")
@Secured("ROLE_USER")
public class UserItemsView extends VerticalLayout {

    @Autowired
    public UserItemsView(ItemService itemService, ItemMapper itemMapper, AppUserService userService,
                         ItemCategoryService itemCategoryService) {
        Grid<ItemDto> grid = new Grid<>();
        refresh(grid, itemService, itemMapper, userService);
        grid.addColumn(ItemDto::getName).setHeader("Name").setSortable(true);
        grid.addColumn(ItemDto::getDescription).setHeader("Description").setSortable(true);
        grid.addColumn(itemDto -> (int)itemDto.getMinPlayers()).setHeader("Minimum players").setSortable(true);
        grid.addColumn(itemDto -> (int)itemDto.getMaxPlayers()).setHeader("Maximum players").setSortable(true);
        grid.addColumn(ItemDto::getCategoryName).setHeader("Category").setSortable(true);

        ItemForm form = new ItemForm(itemMapper, itemService, itemCategoryService);
        HorizontalLayout mainContent = new HorizontalLayout(grid, form);
        mainContent.setSizeFull();
        grid.setSizeFull();

        add(mainContent);
        setSizeFull();
        refresh(grid, itemService, itemMapper, userService);
    }

    private void refresh(Grid grid, ItemService itemService, ItemMapper itemMapper, AppUserService userService) {
        List<ItemDto> userItems = itemMapper.mapToItemDtoList(itemService.findByUserId(getCurrentUserId(userService)));
        grid.setItems(userItems);
    }

    private Long getCurrentUserId(AppUserService userService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return userService.findByName(username).getId();
    }
}
