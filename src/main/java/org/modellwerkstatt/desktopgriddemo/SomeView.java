package org.modellwerkstatt.desktopgriddemo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridMultiSelectionModel;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.gridpro.EditColumnConfigurator;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.gridpro.GridProVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
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

    /* Solved: Why not https://rap.eclipsesource.com/demo/release/rapdemo/#tableviewer
     * Fast Desktop-Style Table ..
     */

    /* Open: GridPro/SelectionGrid speed improvements, mitigate round-tripping?
     *       js: in which cases is this.$server undefined?
     *
     */


    public SomeView() {
        /* Open: Should i rename the component SelectionGrid to DesktopGrid */
        this.setSizeFull();

        /* Open: LUMO_HIGHLIGHT_EDITABLE_CELLS not working, although present in gridglobal.css */
        configureGrid();
        grid.setSizeFull();
        this.add(grid);


        /* Solved: I assume using grid.setItems() is ressource optimal here */
        List<SomeDto> allData = createData(100);
        List<SomeDto> selection = allData.subList(5, 6);
        boolean selectionInData = dataView.setNewList(grid, allData, selection);

        /* Solved: selection via list is not possible, right? */
        LinkedHashSet<SomeDto> collectionAsSet = new LinkedHashSet<>(selection);
        selectionModel.deselectAll();
        selectionModel.updateSelection(collectionAsSet, Collections.emptySet());


        /* Solved: onContextMenu() does the selection */
        GridContextMenu<SomeDto> contextMenu = new GridContextMenu<>(grid);
        contextMenu.addItem("Context menu test", event -> { Notification.show("You clicked the context menu.", 5000, Notification.Position.TOP_CENTER); });


        // Solved: select next item on enter / shift-tab
        grid.getElement().addEventListener("cell-edit-started", e -> {
            grid.disableGlobalEsc();

            int idx = grid.getRowToSelectWhileEdit(e.getEventData());
            if (idx > 0) {
                grid.deselectAll();
                grid.select(dataView.getItem(idx - 1));
            }
        });


        // Open: grid.getEditor().addCancelListener() is not working.
        grid.getElement().addEventListener("cell-edit-stopped", e -> {
            grid.enableGlobalEsc();
        });


        /* Open: The cell is editable, but visualization is not correct
         * Open: scrolling not activated?
         *
         */
        grid.focus();


        /* Open: how to do validation in editable columns, check text() below */
        Button cancelButton = new Button("ESC", e -> {
            Notification.show("You clicked the esc button.", 5000, Notification.Position.TOP_CENTER);
        });

        ShortcutRegistration btnShortcut = Shortcuts.addShortcutListener(this, () -> {
            Notification.show("You triggered the esc button by HK.", 5000, Notification.Position.TOP_CENTER);
        }, Key.ESCAPE);
        btnShortcut.setEventPropagationAllowed(false);
        btnShortcut.setBrowserDefaultAllowed(false);

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


        EditColumnConfigurator<SomeDto> editableCol = grid.addEditColumn(item -> item.value );
        editableCol.text((item, newValue) ->
        {
            try {
                item.value = new BigDecimal(newValue);
            } catch (Exception e) {
                Notification.show("Text not accepted! " + e.getMessage(), 4000, Notification.Position.TOP_END);
            }
        });
        col = editableCol.getColumn();
        col.setHeader("value");
        col.setResizable(true);
    }


    public List<SomeDto> createData (int amount) {
        List<SomeDto> result = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            result.add(new SomeDto(i, "Position " + i, "Article 0", BigDecimal.valueOf(i).multiply(new BigDecimal("0.2"))));
        }

        return result;
    }


}
