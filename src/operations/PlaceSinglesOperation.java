package operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.SingleInStep;
import util.Action;
import util.ConsoleColors;
import util.Printer;

public class PlaceSinglesOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K, SingleInStep<T, K>> {

    private Set<Integer> modifiedIndices;

    public PlaceSinglesOperation(int operationID) {
        super(operationID);
    }

    @Override
    public void completeInitialization() {
        modifiedIndices = new HashSet<>();
        for (SingleInStep<T, K> step : steps) {
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
        if (selection.length() > 0 && selection.charAt(0) == '/') {
            selection = selection.substring(1);
            Integer index = grid.getIndexByCoordinates(selection);
            if (index == null) {
                System.out.println("Invalid cell coordinates: " + selection);
                return Action.NOTHING;
            }
            if (modifiedIndices.contains(index)) {
                K baseCell = grid.get(index);
                baseCell.setColor(ConsoleColors.YELLOW);
                SingleInStep<T, K> step = steps.stream().filter(s -> s.getCellIndex() == index).findFirst().orElse(null);
                if (step == null) {
                    System.out.println("No step found for cell " + selection + ".");
                    return Action.NOTHING;
                }
                for (Integer reasonCell : step.getReasonCellIndices()) {
                    grid.get(reasonCell).setColor(ConsoleColors.RED);
                }

                System.out.println(grid.toString());
                System.out.print("Cell " + Printer.colorWith(grid.getCoordinatesByIndex(index), ConsoleColors.YELLOW) + " has possible values: ");
                for (Integer value : baseCell.getPossibleValues()) {
                    if (value == step.getValue()) System.out.print(Printer.colorWith("" + baseCell.getChar(value), ConsoleColors.GREEN) + " ");
                    else System.out.print(baseCell.getChar(value) + " ");
                }
                System.out.println("\n");
                step.getReasonCellIndices().forEach(reasonIndex -> {
                    K reasonCell = grid.get(reasonIndex);
                    System.out.print("Reason cell " + Printer.colorWith(grid.getCoordinatesByIndex(reasonIndex), ConsoleColors.RED) + " has possible values: ");
                    reasonCell.getPossibleValues().forEach(value -> {
                        System.out.print(reasonCell.getChar(value) + " ");
                    });
                    System.out.println("");
                });

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
        System.out.println(this.operationID + ": Placed single values.\n");
    }
    
}
