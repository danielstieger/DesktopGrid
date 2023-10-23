package org.modellwerkstatt.desktopgriddemo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.gridpro.EditColumnConfigurator;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.Route;
import org.modellwerkstatt.desktopgrid.SelectionGrid;
import org.modellwerkstatt.desktopgrid.SelectionGridDataView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

@Route("/demo/")
public class SomeView extends VerticalLayout {


    private SelectionGridDataView<SomeDto> dataView;
    private SelectionGrid<SomeDto> grid;
    private GridMultiSelectionModel<SomeDto> selectionModel;
    private ShortcutRegistration escShortCut;

    public SomeView() {
        this.setSizeFull();

        configureGrid();
        grid.setSizeFull();
        this.add(grid);

        List<SomeDto> allData = createData(100);
        List<SomeDto> selection = allData.subList(10, 15);
        boolean selectionInData = dataView.setNewList(grid, allData, selection);


        LinkedHashSet<SomeDto> collectionAsSet = new LinkedHashSet<>(selection);
        selectionModel.deselectAll();
        selectionModel.updateSelection(collectionAsSet, Collections.emptySet());



        /* set up cancel button and shortcut on this layout*/
        Button cancelButton = new Button("ESC", e -> {
            Notification.show("You clicked the esc button.", 5000, Notification.Position.TOP_CENTER);
        });
        escShortCut = Shortcuts.addShortcutListener(this, () -> {
            Notification.show("You triggered the esc button by HK.", 5000, Notification.Position.TOP_CENTER);
        }, Key.ESCAPE);

        escShortCut.setEventPropagationAllowed(false);
        escShortCut.setBrowserDefaultAllowed(false);

        this.add(cancelButton);
    }


    public void configureGrid() {
        dataView = new SelectionGridDataView<>();

        grid = new SelectionGrid<>();
        grid.setEditOnClick(true);
        grid.setEnterNextRow(true);
        grid.addThemeVariants(GridProVariant.LUMO_HIGHLIGHT_EDITABLE_CELLS);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.setThemeName("dense");

        selectionModel = (GridMultiSelectionModel<SomeDto>) grid.getSelectionModel();
        selectionModel.setSelectionColumnFrozen(true);

        // posnum
        Grid.Column<SomeDto> col;
        String litPropName = "posNum";
        String template = "<span style=\"${item." + litPropName + "Style}\">${item." + litPropName + "}</span>";

        col = grid.addColumn(LitRenderer.<SomeDto>of(template).
                withProperty(litPropName, item -> {
                    return item.posNum;
                }).
                withProperty(litPropName + "Style", item -> {
                    return "color: #FF0000;";
                }));
        col.setHeader(litPropName);
        col.setResizable(true);

        litPropName = "name";
        template = "<span style=\"${item." + litPropName + "Style}\">${item." + litPropName + "}</span>";

        col = grid.addColumn(LitRenderer.<SomeDto>of(template).
                withProperty(litPropName, item -> {
                    return item.name;
                }).
                withProperty(litPropName + "Style", item -> {
                    return "";
                }));
        col.setHeader(litPropName);
        col.setResizable(true);

        litPropName = "article";
        template = "<span style=\"${item." + litPropName + "Style}\">${item." + litPropName + "}</span>";

        col = grid.addColumn(LitRenderer.<SomeDto>of(template).
                withProperty(litPropName, item -> {
                    return item.article;
                }).
                withProperty(litPropName + "Style", item -> {
                    return "";
                }));
        col.setHeader(litPropName);
        col.setResizable(true);

        litPropName = "value";
        template = "<span style=\"${item." + litPropName + "Style}\">${item." + litPropName + "}</span>";

        col = grid.addColumn(LitRenderer.<SomeDto>of(template).
                withProperty(litPropName, item -> {
                    return item.value;
                }).
                withProperty(litPropName + "Style", item -> {
                    return "";
                }));
        col.setHeader(litPropName);
        col.setResizable(true);
    }


    public List<SomeDto> createData (int amount) {
        List<SomeDto> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            result.add(new SomeDto(i, "Position " + i, "Article 0", new BigDecimal(0.2 * i)));
        }

        return result;
    }


}
