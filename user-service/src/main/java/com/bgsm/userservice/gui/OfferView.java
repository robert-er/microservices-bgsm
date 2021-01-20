package com.bgsm.userservice.gui;

import com.bgsm.userservice.gui.forms.CategoryForm;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.gui.forms.OfferViewForm;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.mapper.OrderMapper;
import com.bgsm.userservice.model.AppUser;
import com.bgsm.userservice.service.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("offerview")
public class OfferView extends VerticalLayout implements HasUrlParameter<String> {

    private String offerId;
    private final ItemCategoryService itemCategoryService;
    private final OfferMapper offerMapper;
    private final OfferService offerService;
    private final ItemMapper itemMapper;
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    private final AppUserService userService;

    @Autowired
    public OfferView(ItemCategoryService itemCategoryService, OfferMapper offerMapper, OfferService offerService,
                     ItemMapper itemMapper, ItemService itemService,
                     OrderService orderService, OrderMapper orderMapper, AppUserService userService) {
        this.itemCategoryService = itemCategoryService;
        this.offerMapper = offerMapper;
        this. offerService = offerService;
        this.itemMapper = itemMapper;
        this.itemService = itemService;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
        this.userService = userService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String offerId) {
        this.offerId = offerId;

        UI.getCurrent().access(() -> {
            MainMenuBar mainMenuBar = new MainMenuBar();
            HorizontalLayout menuLayout = new HorizontalLayout();
            menuLayout.add(mainMenuBar);
            add(menuLayout);

            CategoryForm categoryForm = new CategoryForm(itemCategoryService);
            HorizontalLayout categoryLayout = new HorizontalLayout();
            categoryLayout.add(categoryForm);

            OfferViewForm offerView = new OfferViewForm(offerMapper, offerService, itemMapper, itemService,
                    offerId, orderService, orderMapper, userService);

            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.add(offerView);
            verticalLayout.setSizeFull();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(categoryLayout, verticalLayout);
            horizontalLayout.setSizeFull();

            add(horizontalLayout);
        });
    }
}
