package com.bgsm.userservice.gui;

import com.bgsm.userservice.dto.OfferDto;
import com.bgsm.userservice.gui.forms.CategoryForm;
import com.bgsm.userservice.gui.forms.MainMenuBar;
import com.bgsm.userservice.mapper.OfferMapper;
import com.bgsm.userservice.service.ItemCategoryService;
import com.bgsm.userservice.service.ItemService;
import com.bgsm.userservice.service.OfferService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("category")
public class CategoryView extends VerticalLayout implements HasUrlParameter<String> {

    private String categoryName;
    private final ItemCategoryService itemCategoryService;
    private final ItemService itemService;
    private final OfferService offerService;
    private final OfferMapper offerMapper;

    @Autowired
    public CategoryView(ItemCategoryService itemCategoryService, ItemService itemService,
                        OfferService offerService,
                        OfferMapper offerMapper) {
        this.itemCategoryService = itemCategoryService;
        this.itemService = itemService;
        this.offerService = offerService;
        this.offerMapper = offerMapper;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String parameter) {
        this.categoryName = parameter;
        UI.getCurrent().access(() -> {
            MainMenuBar mainMenuBar = new MainMenuBar();
            HorizontalLayout menuLayout = new HorizontalLayout();
            menuLayout.add(mainMenuBar);
            add(menuLayout);

            CategoryForm categoryForm = new CategoryForm(itemCategoryService);
            HorizontalLayout categoryLayout = new HorizontalLayout();
            categoryLayout.add(categoryForm);

            Text welcomeLabel = new Text("Category: " + categoryName);
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.add(welcomeLabel, getCategoryGrid(categoryName));
            verticalLayout.setSizeFull();

            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.add(categoryLayout, verticalLayout);
            horizontalLayout.setSizeFull();

            add(horizontalLayout);
        });
    }

    private Grid getCategoryGrid(String categoryName) {
        Grid<OfferDto> grid = new Grid<>();
        List<OfferDto> offers = offerMapper.mapToOfferDtoList(offerService.findByCategoryName(categoryName));

        grid.setItems(offers);

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

        grid.addColumn(OfferDto::getId)
                .setSortable(true)
                .setHeader("Offer ID");

        Grid.Column<OfferDto> editorColumn = grid.addComponentColumn(edited -> {
            Button view = new Button("View");
            view.addClickListener(e -> {
             UI.getCurrent().getPage().open("offerview/" + edited.getId(), String.valueOf(edited.getId()));
            });
            return view;
        });

        return grid;
    }
}
