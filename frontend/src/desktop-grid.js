/*
 * modellwerkstatt desktop-grid vaadin gridpro extension
 *
 *
 * Daniel Stieger, Winter 23
 *
 *
 */


window.modellwerkstatt_desktopgrid = {

    focusGrid : function(grid) {
        setTimeout(function() {
         let editDiv = grid.shadowRoot.querySelector("[aria-selected='true'] > div[part~='editable-cell']");
         if (editDiv) {
             editDiv.parentElement.focus();
             // console.log('window.turku.focusGrid() editabled, focussed on ' + editDiv);

         } else {
             let firstTd = grid.shadowRoot.querySelector('[aria-selected="true"] > td');
             firstTd.focus();
             // console.log('window.turku.focusGrid() NOT editabled, focussed on ' + firstTd);

         }

        }, 300);
    },
}
