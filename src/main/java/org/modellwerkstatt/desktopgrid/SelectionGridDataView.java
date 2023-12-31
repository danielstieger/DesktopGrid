package org.modellwerkstatt.desktopgrid;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SelectionGridDataView<DTO> {
    private List<DTO> originalList;
    private List<DTO> filteredList;
    private List<DTO> removedList;

    private GridListDataView currentDataView;
    private String filterForWhat = "";

    public SelectionGridDataView() {

    }


    // returns: need to set selections
    public boolean setNewList(Grid<DTO> grid, List<DTO> newList, List<DTO> selectedObjects) {
        // (0) SelCrtl clears selection if sel not in newList
        // (1) take over list
        // (2) apply filter if necessary, set filteredList
        // (3) Only in case filter is applied: if not all sel in filteredList, clear sel locally.

        originalList = newList;
        boolean allSelectionsFound = applyFilterAndRefreshGrid(selectedObjects);
        // !! selections are cleared by instantiating a new dataView
        currentDataView = grid.setItems(filteredList);

        return allSelectionsFound;
    }

    // returns: need to set selections
    public boolean updateFilterList(Grid<DTO> grid, Set<DTO> currentTableSelection){
        boolean allSelectionsFound = applyFilterAndRefreshGrid(currentTableSelection);
        // !! selections are cleared by instantiating a new dataView
        currentDataView = grid.setItems(filteredList);

        return allSelectionsFound;
    }

    public boolean allSelectionsCurrentlyInFilter(Set<DTO> currentTableSelection){
        int selectionSize = currentTableSelection.size();
        int foundSel = 0;

        for (DTO item: filteredList){
            if (currentTableSelection.contains(item)) {
                foundSel ++;
            }

            if (foundSel >= selectionSize) { break; }
        }

        return foundSel == selectionSize;
    }

    private boolean applyFilterAndRefreshGrid(Collection<DTO> selection) {
        if (isNoFilter()) {
            filteredList = originalList;
            removedList = new ArrayList<>();
            return true;

        } else {
            filteredList = new ArrayList<>();
            removedList = new ArrayList<>();

            int selectionsFound = 0;

            for (DTO item: originalList) {
                if (textInDto(item)){
                    filteredList.add(item);
                    if (selection.contains(item)) { selectionsFound++; }
                } else {
                    removedList.add(item);
                }
            }
            return selection.size() == selectionsFound;
        }
    }

    private boolean textInDto(DTO item){
        String text = item.toString();

        if (text.toLowerCase().contains(filterForWhat)) { return true; }
        return false;
    }



    public int getFilteredTotalCount() {
        return filteredList.size();
    }

    public void setSearchText(String text) {
        filterForWhat = text.toLowerCase().replace(".", "");
    }

    private boolean isNoFilter(){
        return "".equals(filterForWhat);
    }

    public DTO getItem(int index) {
        return filteredList.get(index);
    }

    public int getIndex(DTO item) {
        return filteredList.indexOf(item);
    }

    public List<DTO> getFilteredList() {
        return filteredList;
    }
}

