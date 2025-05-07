package operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.RemovePointingStep;
import util.Action;
import util.ConsoleColors;
import util.Printer;

public class RemovePointingOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K, RemovePointingStep<T, K>> {

    private Set<Integer> modifiedIndices;

    public RemovePointingOperation(int operationID) {
        super(operationID);
    }

    @Override
    public void completeInitialization() {
        modifiedIndices = new HashSet<>();
        for (RemovePointingStep<T, K> step : steps) {
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
                

                for (RemovePointingStep<T, K> step : steps) {
                    if (step.getCellIndex() == index) {
                        
                        baseCell.setColor(ConsoleColors.YELLOW);
                        List<Integer> reasonCellIndices = step.getReasonCellIndices();
                        reasonCellIndices.forEach(nullCellIndex -> {
                            grid.get(nullCellIndex).setColor(ConsoleColors.RED);
                        });
                        List<Integer> pointingCellIndices = step.getPointingCellIndices();
                        pointingCellIndices.forEach(pointingCellIndex -> {
                            grid.get(pointingCellIndex).setColor(ConsoleColors.GREEN);
                        });
                        List<Integer> toPurpleCellIndices = step.getToPurpleCellIndices();
                        toPurpleCellIndices.forEach(toPurpleCellIndex -> {
                            grid.get(toPurpleCellIndex).setColor(ConsoleColors.PURPLE);
                        });
                        
                        System.out.println(grid.toString());
                        
                        System.out.println("Value " + Printer.colorWith(""+baseCell.getChar(step.getValue()), ConsoleColors.RED) + " was removed because of pointing cells:\n");

                        reasonCellIndices.forEach(reasonCellIndex -> {
                            K reasonCell = grid.get(reasonCellIndex);
                            System.out.print("Cell " + Printer.colorWith(grid.getCoordinatesByIndex(reasonCellIndex), ConsoleColors.RED) + " has possible values: ");
                            for (Integer value : reasonCell.getPossibleValues()) {
                                System.out.print(reasonCell.getChar(value) + " ");
                            }
                            System.out.println("");
                        });
                        pointingCellIndices.forEach(pointingCellIndex -> {
                            K pointingCell = grid.get(pointingCellIndex);
                            System.out.print("Cell " + Printer.colorWith(grid.getCoordinatesByIndex(pointingCellIndex), ConsoleColors.GREEN) + " has possible values: ");
                            for (Integer value : pointingCell.getPossibleValues()) {
                                if (value == step.getValue()) {
                                    System.out.print(Printer.colorWith("" + pointingCell.getChar(value), ConsoleColors.GREEN) + " ");
                                }
                                else {
                                    System.out.print(pointingCell.getChar(value) + " ");
                                }
                            }
                            System.out.println("");
                        });
                        System.out.println("=>");

                        System.out.print("Cell " + Printer.colorWith(grid.getCoordinatesByIndex(index), ConsoleColors.YELLOW) + " has possible values: ");
                        System.out.print(Printer.colorWith("" + baseCell.getChar(step.getValue()), ConsoleColors.RED) + " ");
                        for (Integer value : baseCell.getPossibleValues()) {
                            System.out.print(baseCell.getChar(value) + " ");
                        }
                        System.out.println("");
                        Printer.getInput();
                    }
                }
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
        System.out.println(this.operationID + "/" + historySize + ": Reduced possible values dew to pointing cells.\n");
    }
    
}
