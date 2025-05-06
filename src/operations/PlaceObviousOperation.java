package operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.PlaceObviousStep;
import util.Action;
import util.ConsoleColors;
import util.Printer;

public class PlaceObviousOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K, PlaceObviousStep<T, K>> {

    private Set<Integer> modifiedIndices;

    public PlaceObviousOperation(int operationID) {
        super(operationID);
    }

    @Override
    public void completeInitialization() {
        modifiedIndices = new HashSet<>();
        for (PlaceObviousStep<T, K> step : steps) {
            modifiedIndices.add(step.getCellIndex());
        }
    }

    @Override
    public boolean isDoingNothing() {
        return modifiedIndices.isEmpty();
    }

    @Override
    public Map<String, String> getSelectionOptions(T grid) {
        Map<String, String> s = new HashMap<>();
        s.put("/" + grid.getCoordinatesPrompt(), "Get explanation on cell.");
        return s;
    }

    @Override
    public Action doSelection(String selection, T grid) {
        if (selection.charAt(0) == '/') {
            selection = selection.substring(1);
            Integer index = grid.getIndexByCoordinates(selection);
            if (index == null) {
                System.out.println("Invalid cell coordinates: " + selection);
                return Action.NOTHING;
            }
            if (modifiedIndices.contains(index)) {
                K baseCell = grid.get(index);
                baseCell.setColor(ConsoleColors.YELLOW);
                System.out.println(grid.toString());
                System.out.println("Cell " + grid.getCoordinatesByIndex(index) + " has possible values: " + baseCell.getValues());

                Printer.getInput();
            }
            else {
                System.out.println("No explanation available for cell " + selection + ".");
            }
        }
        return Action.NOTHING;
    }

    @Override
    public void printGrid(T grid) {

        for (Integer index : modifiedIndices) {
            K cell = grid.get(index);
            if (cell != null) {
                cell.setColor(ConsoleColors.YELLOW);
            }
        }
        System.out.println(grid.toString());
        System.out.println(this.operationID + ": Placed obvious values.\n");
    }
    
}
