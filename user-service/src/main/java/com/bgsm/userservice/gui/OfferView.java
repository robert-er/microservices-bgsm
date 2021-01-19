package com.bgsm.userservice.gui;

import com.bgsm.userservice.gui.forms.CategoryForm;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.gui.forms.OfferViewForm;
import com.bgsm.userservice.mapper.ItemMapper;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.service.ItemCategoryService;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
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

    @Autowired
    public OfferView(ItemCategoryService itemCategoryService, OfferMapper offerMapper, OfferService offerService,
                     ItemMapper itemMapper, ItemService itemService) {
        this.itemCategoryService = itemCategoryService;
        this.offerMapper = offerMapper;
        this. offerService = offerService;
        this.itemMapper = itemMapper;
        this.itemService = itemService;
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
                    offerId);

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
