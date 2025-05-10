package operations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import grids.AbstractGrid;
import grids.cells.AbstractCell;
import operations.steps.RemovePossibleHGroupStep;
import util.Action;
import util.ConsoleColors;
import util.Printer;

public class RemoveOutsideHGroupsOperation<T extends AbstractGrid<T, K>, K extends AbstractCell<K>> extends AbstractOperation<T, K, RemovePossibleHGroupStep<T, K>> {

    private Set<Integer> modifiedIndices;

    public RemoveOutsideHGroupsOperation(int operationID) {
        super(operationID);
    }

    @Override
    public void completeInitialization() {
        modifiedIndices = new HashSet<>();
        for (RemovePossibleHGroupStep<T, K> step : steps) {
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
                

                for (RemovePossibleHGroupStep<T, K> step : steps) {
                    if (step.getCellIndex() == index) {
                        
                        List<Integer> groupCellIndices = step.getGroupCellIndices();
                        groupCellIndices.forEach(groupCellIndex -> {
                            grid.get(groupCellIndex).setColor(ConsoleColors.GREEN);
                        });
                        List<Integer> nonGroupCellIndices = step.getNonGroupCellIndices();
                        nonGroupCellIndices.forEach(nonGroupCellIndex -> {
                            grid.get(nonGroupCellIndex).setColor(ConsoleColors.RED);
                        });
                        List<Integer> toPurple = step.getToPurple();
                        toPurple.forEach(toPurpleIndex -> {
                            grid.get(toPurpleIndex).setColor(ConsoleColors.PURPLE);
                        });
                        baseCell.setColor(ConsoleColors.YELLOW);
                        System.out.println(grid.toString());
                        
                        Set<Integer> groupValues = new HashSet<>();
                        groupCellIndices.forEach(cellIndex -> {
                            K cell = grid.get(cellIndex);
                            groupValues.addAll(cell.getPossibleValues());
                        });

                        System.out.print("Group cells can only contain values ");
                        groupValues.forEach(value -> {
                            System.out.print(Printer.colorWith(""+baseCell.getChar(value), ConsoleColors.YELLOW) + " ");
                        });
                        System.out.println(", so no other places are allowed\n");
                        
                        groupCellIndices.forEach(reasonCellIndex -> {
                            K reasonCell = grid.get(reasonCellIndex);
                            System.out.print("Group cell " + Printer.colorWith(grid.getCoordinatesByIndex(reasonCellIndex), ConsoleColors.GREEN) + " has possible values: ");
                            for (Integer value : reasonCell.getPossibleValues()) {
                                System.out.print(Printer.colorWith("" + reasonCell.getChar(value), ConsoleColors.GREEN) + " ");
                            }
                            System.out.println("");
                        });
                        nonGroupCellIndices.forEach(pointingCellIndex -> {
                            K pointingCell = grid.get(pointingCellIndex);
                            System.out.print("Non group cell " + Printer.colorWith(grid.getCoordinatesByIndex(pointingCellIndex), ConsoleColors.RED) + " has possible values: ");
                            for (Integer value : pointingCell.getPossibleValues()) {
                                System.out.print(pointingCell.getChar(value) + " ");
                            }
                            System.out.println("");
                        });
                        System.out.println("=>");
                        
                        System.out.print("Value " + Printer.colorWith(""+baseCell.getChar(step.getValue()), ConsoleColors.RED) + " was removed from cell ");
                        System.out.println(Printer.colorWith(grid.getCoordinatesByIndex(step.getCellIndex()), ConsoleColors.YELLOW));
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
        System.out.println(this.operationID + "/" + historySize + ": Reduced possible values outside hidden groups.\n");
    }
    
}
