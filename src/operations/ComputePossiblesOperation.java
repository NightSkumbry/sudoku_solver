package operations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.RemovePossibleStep;
import util.Action;
import util.ConsoleColors;
import util.Printer;

public class ComputePossiblesOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K, RemovePossibleStep<T, K>> {

    private Set<Integer> modifiedIndices;

    public ComputePossiblesOperation(int operationID) {
        super(operationID);
    }

    @Override
    public void completeInitialization() {
        modifiedIndices = new HashSet<>();
        for (RemovePossibleStep<T, K> step : steps) {
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

    private void printInfo(int index, List<Integer> reasonCells, List<Integer> values, T grid) {
        K baseCell = grid.get(index);
        baseCell.setColor(ConsoleColors.YELLOW);
        for (Integer reasonCell : reasonCells) {
            grid.get(reasonCell).setColor(ConsoleColors.RED);
        }
        System.out.println(grid.toString());

        Set<Integer> possibles = baseCell.getPossibleValues();
        System.out.print("Cell " + Printer.colorWith(grid.getCoordinatesByIndex(index), ConsoleColors.YELLOW) + " has possible values: ");
        for (Integer value : possibles) {
            System.out.print(baseCell.getChar(value) + " ");
        }
        for (Integer value : values) {
            System.out.print(ConsoleColors.RED.getSequence() + baseCell.getChar(value) + ConsoleColors.RESET.getSequence() + " ");
        }
        System.out.println("\n");
    }

    @Override
    public Action doSelection(String selection, T grid) {
        if (selection.length() > 0 && selection.charAt(0) == '/') {
            selection = selection.substring(1);
            Integer index = grid.getIndexByCoordinates(selection);
            if (index == null) {
                System.out.println("Invalid cell coordinates: " + selection);
                return Action.NOTHING;
            }
            if (modifiedIndices.contains(index)) {
                List<Integer> reasonCells = new ArrayList<>();
                List<Integer> values = new ArrayList<>();
                for (RemovePossibleStep<T, K> step : steps) {
                    if (step.getCellIndex() == index) {
                        reasonCells.add(step.getReasonCell());
                        values.add(step.getValue());
                    }
                }

                printInfo(index, reasonCells, values, grid);

                Printer.getInput();
            }
            else {
                System.out.println("No explanation available for cell " + selection + ".");
            }
        }
        return Action.NOTHING;
    }

    @Override
    public void printGrid(T grid, int historySize) {

        for (Integer index : modifiedIndices) {
            K cell = grid.get(index);
            if (cell != null) {
                cell.setColor(ConsoleColors.YELLOW);
            }
        }
        System.out.println(grid.toString());
        System.out.println(this.operationID + "/" + historySize + ": Possible values recomputed.\n");
    }
    
}
