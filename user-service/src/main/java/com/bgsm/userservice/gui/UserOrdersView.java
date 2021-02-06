package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.CartDto;
import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.dto.OrderDto;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.mapper.OrderMapper;
import com.bgsm.userservice.service.AppUserService;
import com.bgsm.userservice.service.CartService;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@Route("userorders")
@Secured("USER")
public class UserOrdersView extends VerticalLayout {

    @Autowired
    public UserOrdersView(OrderMapper orderMapper, CartService cartService, AppUserService userService,
                          OfferService offerService,
                          ItemService itemService,
                          OfferMapper offerMapper) {
        MainMenuBar mainMenuBar = new MainMenuBar();
        HorizontalLayout menuLayout = new HorizontalLayout();
        menuLayout.add(mainMenuBar);
        add(menuLayout);

        Grid<OfferDto> grid = new Grid<>();
        CartDto cartDto = new CartDto();
        OrderDto orderDto = new OrderDto();

        Binder<OfferDto> binder = new Binder<>(OfferDto.class);
        List<OrderDto> userOrders = orderMapper
                .mapToOrderDtoList(cartService.getUserOrders(getCurrentUserId(userService)));
        List<OfferDto> userOrderedOffers = offerMapper.mapToOfferDtoList(
                userOrders.stream()
                        .map(orderDto1 -> offerService.findById(orderDto1.getOfferId()))
                        .collect(Collectors.toList())
        );
        grid.setItems(userOrderedOffers);

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
        grid.addColumn(offerDto -> itemService.findById(offerDto.getItemId()).getUser().getUsername())
                .setSortable(true)
                .setHeader("Game Owner");

        add(grid);
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
