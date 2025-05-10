package sudokues;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grids.ClassicGrid;
import grids.cells.ClassicCell;
import operations.ComputePossiblesOperation;
import operations.PlaceObviousOperation;
import operations.PlaceSinglesOperation;
import operations.RemoveInHGroupsOperation;
import operations.RemoveOutsideHGroupsOperation;
import operations.RemovePointingOperation;
import operations.steps.PlaceObviousStep;
import operations.steps.RemovePossibleHGroupStep;
import operations.steps.RemovePossibleInPointingStep;
import operations.steps.RemovePossibleStep;
import operations.steps.SingleInStep;
import util.ConsoleColors;
import util.Printer;

public class ClassicSudoku extends AbstractSudoku<ClassicGrid, ClassicCell> {
    public ClassicSudoku(List<Integer> grid) {
        super(new ClassicGrid(grid));
    }

    @Override
    public void findOneSolution() {
        System.out.println("Finding one solution...");
        if (fullySolved) {
            System.out.println("Grid is already fully solved.");
            return;
        }
        while (true) {
            
            this.currentGrid = bufferGrid.copy();
            if (!this.currentGrid.isValid()) {
                System.out.println(Printer.colorWith("Grid is not valid right now!.", ConsoleColors.RED));
                break;
            }
            computePossibles();
            this.bufferGrid = currentGrid.copy();
            
            if (placeSingles()) continue;
            
            if (placeObvious()) continue;

            if (computePointingValues()) continue;

            if (computeHGroups()) continue;

            break;
        }

        if (isFull()) {
            System.out.println("Grid is fully solved.");
            return;
        }
        System.out.println("Grid is not fully solved yet. But I can't find any more values.");
    }

    private boolean isFull() {
        fullySolved =  currentGrid.isFull() && currentGrid.isValid(); //TODO: redo when implementing branching
        return fullySolved;
    }
    
    @Override
    public void findAllSolutions() {
        while (!fullySolved) {
            findOneSolution();
        }
    }


    private boolean computeHGroups() {
        if (computeInHGroups(2)) return true; // H - means hidden

        if (computeOutsideHGroups(2)) return true;

        if (computeInHGroups(3)) return true; // H - means hidden

        if (computeOutsideHGroups(3)) return true;

        if (computeInHGroups(4)) return true; // H - means hidden

        if (computeOutsideHGroups(4)) return true;

        return false;
    }

    private boolean computeInHGroups(int groupSize) {
        System.out.println("Computing inside groups of size " + groupSize);

        RemoveInHGroupsOperation<ClassicGrid, ClassicCell> removeInHGroupsOperation = new RemoveInHGroupsOperation<>(history.getNextOperationID());

        // check rows
        for (int row = 0; row < 9; row++) {
            List<ClassicCell> rowCells = currentGrid.getRow(row);
            Set<Integer> rowPossibleValues = new HashSet<>();
            rowCells.forEach(rowCell -> {
                if (!rowCell.isSet()) rowPossibleValues.addAll(rowCell.getPossibleValues());
            });
            if (rowPossibleValues.size() <= groupSize) continue;
            for (Set<Integer> valueCombination : Sets.combinations(rowPossibleValues, groupSize)) {
                List<Integer> possibleCellIndices = new ArrayList<>();
                for (int rowCellIndex = 0; rowCellIndex < 9; rowCellIndex++) {
                    for (int v : valueCombination) {
                        if (rowCells.get(rowCellIndex).getPossibleValues().contains(v)) {
                            possibleCellIndices.add(rowCellIndex);
                            break;
                        }
                    }
                }
                
                if (possibleCellIndices.size() <= valueCombination.size()) {
                    for (int rowCellIndex = 0; rowCellIndex < 9; rowCellIndex++) {
                        ClassicCell cell = rowCells.get(rowCellIndex);
                        if (cell.isSet() || !possibleCellIndices.contains(rowCellIndex)) continue;
                        for (int i = 0; i < 9; i++) {
                            if (valueCombination.contains(i)) continue;
                            int fullCellIndex = currentGrid.getIndexByRowIndex(row, rowCellIndex);
                            boolean r = bufferGrid.get(fullCellIndex).removePossibleValue(i);
                            if (r) {
                                List<Integer> groupIndices = new ArrayList<>();
                                List<Integer> nonGroupIndices = new ArrayList<>();
                                List<Integer> toPurple = new ArrayList<>();
                                for (int rcl = 0; rcl < 9; rcl++) {
                                    if (possibleCellIndices.contains(rcl)) groupIndices.add(currentGrid.getIndexByRowIndex(row, rcl));
                                    else if (!rowCells.get(rcl).isSet()) nonGroupIndices.add(currentGrid.getIndexByRowIndex(row, rcl));
                                    else toPurple.add(currentGrid.getIndexByRowIndex(row, rcl));
                                }
                                removeInHGroupsOperation.addStep(new RemovePossibleHGroupStep<>(fullCellIndex, i, groupIndices, nonGroupIndices, toPurple));
                            }
                        }
                    }
                }
            }
        }

        // check columns
        for (int column = 0; column < 9; column++) {
            List<ClassicCell> columnCells = currentGrid.getColumn(column);
            Set<Integer> columnPossibleValues = new HashSet<>();
            columnCells.forEach(columnCell -> {
                if (!columnCell.isSet()) columnPossibleValues.addAll(columnCell.getPossibleValues());
            });
            if (columnPossibleValues.size() <= groupSize) continue;
            for (Set<Integer> valueCombination : Sets.combinations(columnPossibleValues, groupSize)) {
                List<Integer> possibleCellIndices = new ArrayList<>();
                for (int columnCellIndex = 0; columnCellIndex < 9; columnCellIndex++) {
                    for (int v : valueCombination) {
                        if (columnCells.get(columnCellIndex).getPossibleValues().contains(v)) {
                            possibleCellIndices.add(columnCellIndex);
                            break;
                        }
                    }
                }
                
                if (possibleCellIndices.size() <= valueCombination.size()) {
                    for (int columnCellIndex = 0; columnCellIndex < 9; columnCellIndex++) {
                        ClassicCell cell = columnCells.get(columnCellIndex);
                        if (cell.isSet() || !possibleCellIndices.contains(columnCellIndex)) continue;
                        for (int i = 0; i < 9; i++) {
                            if (valueCombination.contains(i)) continue;
                            int fullCellIndex = currentGrid.getIndexByColumnIndex(column, columnCellIndex);
                            boolean r = bufferGrid.get(fullCellIndex).removePossibleValue(i);
                            if (r) {
                                List<Integer> groupIndices = new ArrayList<>();
                                List<Integer> nonGroupIndices = new ArrayList<>();
                                List<Integer> toPurple = new ArrayList<>();
                                for (int rcl = 0; rcl < 9; rcl++) {
                                    if (possibleCellIndices.contains(rcl)) groupIndices.add(currentGrid.getIndexByColumnIndex(column, rcl));
                                    else if (!columnCells.get(rcl).isSet()) nonGroupIndices.add(currentGrid.getIndexByColumnIndex(column, rcl));
                                    else toPurple.add(currentGrid.getIndexByColumnIndex(column, rcl));
                                }
                                removeInHGroupsOperation.addStep(new RemovePossibleHGroupStep<>(fullCellIndex, i, groupIndices, nonGroupIndices, toPurple));
                            }
                        }
                    }
                }
            }
        }

        // check boxes
        for (int box = 0; box < 9; box++) {
            List<ClassicCell> boxCells = currentGrid.getBox(box);
            Set<Integer> boxPossibleValues = new HashSet<>();
            boxCells.forEach(boxCell -> {
                if (!boxCell.isSet()) boxPossibleValues.addAll(boxCell.getPossibleValues());
            });
            if (boxPossibleValues.size() <= groupSize) continue;
            for (Set<Integer> valueCombination : Sets.combinations(boxPossibleValues, groupSize)) {
                List<Integer> possibleCellIndices = new ArrayList<>();
                for (int boxCellIndex = 0; boxCellIndex < 9; boxCellIndex++) {
                    for (int v : valueCombination) {
                        if (boxCells.get(boxCellIndex).getPossibleValues().contains(v)) {
                            possibleCellIndices.add(boxCellIndex);
                            break;
                        }
                    }
                }
                
                if (possibleCellIndices.size() <= valueCombination.size()) {
                    for (int boxCellIndex = 0; boxCellIndex < 9; boxCellIndex++) {
                        ClassicCell cell = boxCells.get(boxCellIndex);
                        if (cell.isSet() || !possibleCellIndices.contains(boxCellIndex)) continue;
                        for (int i = 0; i < 9; i++) {
                            if (valueCombination.contains(i)) continue;
                            int fullCellIndex = currentGrid.getIndexByBoxIndex(box, boxCellIndex);
                            boolean r = bufferGrid.get(fullCellIndex).removePossibleValue(i);
                            if (r) {
                                List<Integer> groupIndices = new ArrayList<>();
                                List<Integer> nonGroupIndices = new ArrayList<>();
                                List<Integer> toPurple = new ArrayList<>();
                                for (int rcl = 0; rcl < 9; rcl++) {
                                    if (possibleCellIndices.contains(rcl)) groupIndices.add(currentGrid.getIndexByBoxIndex(box, rcl));
                                    else if (!boxCells.get(rcl).isSet()) nonGroupIndices.add(currentGrid.getIndexByBoxIndex(box, rcl));
                                    else toPurple.add(currentGrid.getIndexByBoxIndex(box, rcl));
                                }
                                removeInHGroupsOperation.addStep(new RemovePossibleHGroupStep<>(fullCellIndex, i, groupIndices, nonGroupIndices, toPurple));
                            }
                        }
                    }
                }
            }
        }
        
        removeInHGroupsOperation.completeInitialization();
        if (removeInHGroupsOperation.isDoingNothing()) {
            return false;
        }
        history.addOperation(removeInHGroupsOperation);
        return true;
    }

    private boolean computeOutsideHGroups(int groupSize) {
        System.out.println("Computing outside groups of size " + groupSize);

        RemoveOutsideHGroupsOperation<ClassicGrid, ClassicCell> removeOutsideHGroupsOperation = new RemoveOutsideHGroupsOperation<>(history.getNextOperationID());

        // check rows
        for (int row = 0; row < 9; row++) {
            List<ClassicCell> rowCells = currentGrid.getRow(row);
            Set<Integer> rowEmptyCells = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                if (!rowCells.get(i).isSet()) rowEmptyCells.add(i);
            }
            if (rowEmptyCells.size() <= groupSize) continue;
            for (Set<Integer> cellCombination : Sets.combinations(rowEmptyCells, groupSize)) {
                Set<Integer> possibleValues = new HashSet<>();
                for (int rowCellIndex : cellCombination) {
                    possibleValues.addAll(rowCells.get(rowCellIndex).getPossibleValues());
                }
                
                if (possibleValues.size() <= cellCombination.size()) {
                    for (int rowCellIndex = 0; rowCellIndex < 9; rowCellIndex++) {
                        ClassicCell cell = rowCells.get(rowCellIndex);
                        if (cell.isSet() || cellCombination.contains(rowCellIndex)) continue;
                        for (int i = 0; i < 9; i++) {
                            if (!possibleValues.contains(i)) continue;
                            int fullCellIndex = currentGrid.getIndexByRowIndex(row, rowCellIndex);
                            boolean r = bufferGrid.get(fullCellIndex).removePossibleValue(i);
                            if (r) {
                                List<Integer> groupIndices = new ArrayList<>();
                                List<Integer> nonGroupIndices = new ArrayList<>();
                                List<Integer> toPurple = new ArrayList<>();
                                for (int rcl = 0; rcl < 9; rcl++) {
                                    if (cellCombination.contains(rcl)) groupIndices.add(currentGrid.getIndexByRowIndex(row, rcl));
                                    else if (!rowCells.get(rcl).isSet()) nonGroupIndices.add(currentGrid.getIndexByRowIndex(row, rcl));
                                    else toPurple.add(currentGrid.getIndexByRowIndex(row, rcl));
                                }
                                removeOutsideHGroupsOperation.addStep(new RemovePossibleHGroupStep<>(fullCellIndex, i, groupIndices, nonGroupIndices, toPurple));
                            }
                        }
                    }
                }
            }
        }

        // check columns
        for (int column = 0; column < 9; column++) {
            List<ClassicCell> columnCells = currentGrid.getColumn(column);
            Set<Integer> columnEmptyCells = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                if (!columnCells.get(i).isSet()) columnEmptyCells.add(i);
            }
            if (columnEmptyCells.size() <= groupSize) continue;
            for (Set<Integer> cellCombination : Sets.combinations(columnEmptyCells, groupSize)) {
                Set<Integer> possibleValues = new HashSet<>();
                for (int columnCellIndex : cellCombination) {
                    possibleValues.addAll(columnCells.get(columnCellIndex).getPossibleValues());
                }
                
                if (possibleValues.size() <= cellCombination.size()) {
                    for (int columnCellIndex = 0; columnCellIndex < 9; columnCellIndex++) {
                        ClassicCell cell = columnCells.get(columnCellIndex);
                        if (cell.isSet() || cellCombination.contains(columnCellIndex)) continue;
                        for (int i = 0; i < 9; i++) {
                            if (!possibleValues.contains(i)) continue;
                            int fullCellIndex = currentGrid.getIndexByColumnIndex(column, columnCellIndex);
                            boolean r = bufferGrid.get(fullCellIndex).removePossibleValue(i);
                            if (r) {
                                List<Integer> groupIndices = new ArrayList<>();
                                List<Integer> nonGroupIndices = new ArrayList<>();
                                List<Integer> toPurple = new ArrayList<>();
                                for (int rcl = 0; rcl < 9; rcl++) {
                                    if (cellCombination.contains(rcl)) groupIndices.add(currentGrid.getIndexByColumnIndex(column, rcl));
                                    else if (!columnCells.get(rcl).isSet()) nonGroupIndices.add(currentGrid.getIndexByColumnIndex(column, rcl));
                                    else toPurple.add(currentGrid.getIndexByColumnIndex(column, rcl));
                                }
                                removeOutsideHGroupsOperation.addStep(new RemovePossibleHGroupStep<>(fullCellIndex, i, groupIndices, nonGroupIndices, toPurple));
                            }
                        }
                    }
                }
            }
        }

        // check boxes
        for (int box = 0; box < 9; box++) {
            List<ClassicCell> boxCells = currentGrid.getBox(box);
            Set<Integer> boxEmptyCells = new HashSet<>();
            for (int i = 0; i < 9; i++) {
                if (!boxCells.get(i).isSet()) boxEmptyCells.add(i);
            }
            if (boxEmptyCells.size() <= groupSize) continue;
            for (Set<Integer> cellCombination : Sets.combinations(boxEmptyCells, groupSize)) {
                Set<Integer> possibleValues = new HashSet<>();
                for (int boxCellIndex : cellCombination) {
                    possibleValues.addAll(boxCells.get(boxCellIndex).getPossibleValues());
                }
                
                if (possibleValues.size() <= cellCombination.size()) {
                    for (int boxCellIndex = 0; boxCellIndex < 9; boxCellIndex++) {
                        ClassicCell cell = boxCells.get(boxCellIndex);
                        if (cell.isSet() || cellCombination.contains(boxCellIndex)) continue;
                        for (int i = 0; i < 9; i++) {
                            if (!possibleValues.contains(i)) continue;
                            int fullCellIndex = currentGrid.getIndexByBoxIndex(box, boxCellIndex);
                            boolean r = bufferGrid.get(fullCellIndex).removePossibleValue(i);
                            if (r) {
                                List<Integer> groupIndices = new ArrayList<>();
                                List<Integer> nonGroupIndices = new ArrayList<>();
                                List<Integer> toPurple = new ArrayList<>();
                                for (int rcl = 0; rcl < 9; rcl++) {
                                    if (cellCombination.contains(rcl)) groupIndices.add(currentGrid.getIndexByBoxIndex(box, rcl));
                                    else if (!boxCells.get(rcl).isSet()) nonGroupIndices.add(currentGrid.getIndexByBoxIndex(box, rcl));
                                    else toPurple.add(currentGrid.getIndexByBoxIndex(box, rcl));
                                }
                                removeOutsideHGroupsOperation.addStep(new RemovePossibleHGroupStep<>(fullCellIndex, i, groupIndices, nonGroupIndices, toPurple));
                            }
                        }
                    }
                }
            }
        }

        removeOutsideHGroupsOperation.completeInitialization();
        if (removeOutsideHGroupsOperation.isDoingNothing()) {
            return false;
        }
        history.addOperation(removeOutsideHGroupsOperation);
        return true;
    }

    private boolean computePointingValues() {
        System.out.println("Computing pointing values...");

        RemovePointingOperation<ClassicGrid, ClassicCell> removePointingOperation = new RemovePointingOperation<>(history.getNextOperationID());

        // check single box cell possible in row
        for (int row = 0; row < 9; row++) {
            List<ClassicCell> rowCells = currentGrid.getRow(row);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possibleBoxPlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = rowCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possibleBoxPlaces.add(currentGrid.getBoxIndex(currentGrid.getIndexByRowIndex(row, k)));
                    }
                }
                if (possibleBoxPlaces.size() == 1) {
                    int boxIndex = possibleBoxPlaces.iterator().next();

                    List<Integer> pointingCellIndices = new ArrayList<>();
                    List<Integer> reasonCellIndices = new ArrayList<>();
                    List<Integer> toPurpleCellIndices = new ArrayList<>();
                    for (int k = 0; k < 9; k++) {
                        ClassicCell cell = rowCells.get(k);
                        if (!cell.isSet() && (currentGrid.getBoxIndex(currentGrid.getIndexByRowIndex(row, k)) != boxIndex || !cell.getPossibleValues().contains(n))) {
                            reasonCellIndices.add(currentGrid.getIndexByRowIndex(row, k));
                        }
                        else if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                            pointingCellIndices.add(currentGrid.getIndexByRowIndex(row, k));
                        }
                        else toPurpleCellIndices.add(currentGrid.getIndexByRowIndex(row, k));
                    }
                        
                    for (int boxCellIndex=0; boxCellIndex < 9; boxCellIndex++) {
                        int cellIndex = currentGrid.getIndexByBoxIndex(boxIndex, boxCellIndex);
                        if (!pointingCellIndices.contains(cellIndex) && !currentGrid.get(cellIndex).isSet()) {
                            boolean r = bufferGrid.get(cellIndex).removePossibleValue(n);
                            if (r) {
                                removePointingOperation.addStep(new RemovePossibleInPointingStep<>(cellIndex, n, reasonCellIndices, pointingCellIndices, toPurpleCellIndices));
                            }
                        }
                    }
                }
            }
        }

        // check single box cell possible in column
        for (int column = 0; column < 9; column++) {
            List<ClassicCell> columnCells = currentGrid.getColumn(column);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possibleBoxPlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = columnCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possibleBoxPlaces.add(currentGrid.getBoxIndex(currentGrid.getIndexByColumnIndex(column, k)));
                    }
                }
                if (possibleBoxPlaces.size() == 1) {
                    int boxIndex = possibleBoxPlaces.iterator().next();

                    List<Integer> pointingCellIndices = new ArrayList<>();
                    List<Integer> reasonCellIndices = new ArrayList<>();
                    List<Integer> toPurpleCellIndices = new ArrayList<>();
                    for (int k = 0; k < 9; k++) {
                        ClassicCell cell = columnCells.get(k);
                        if (!cell.isSet() && (currentGrid.getBoxIndex(currentGrid.getIndexByColumnIndex(column, k)) != boxIndex || !cell.getPossibleValues().contains(n))) {
                            reasonCellIndices.add(currentGrid.getIndexByColumnIndex(column, k));
                        }
                        else if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                            pointingCellIndices.add(currentGrid.getIndexByColumnIndex(column, k));
                        }
                        else toPurpleCellIndices.add(currentGrid.getIndexByColumnIndex(column, k));
                    }
                        
                    for (int boxCellIndex=0; boxCellIndex < 9; boxCellIndex++) {
                        int cellIndex = currentGrid.getIndexByBoxIndex(boxIndex, boxCellIndex);
                        if (!pointingCellIndices.contains(cellIndex) && !currentGrid.get(cellIndex).isSet()) {
                            boolean r = bufferGrid.get(cellIndex).removePossibleValue(n);
                            if (r) {
                                removePointingOperation.addStep(new RemovePossibleInPointingStep<>(cellIndex, n, reasonCellIndices, pointingCellIndices, toPurpleCellIndices));
                            }
                        }
                    }
                }
            }
        }

        // check single row cell possible in box
        for (int box = 0; box < 9; box++) {
            List<ClassicCell> boxCells = currentGrid.getBox(box);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possibleRowPlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = boxCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possibleRowPlaces.add(currentGrid.getRowIndex(currentGrid.getIndexByBoxIndex(box, k)));
                    }
                }
                if (possibleRowPlaces.size() == 1) {
                    int rowIndex = possibleRowPlaces.iterator().next();

                    List<Integer> pointingCellIndices = new ArrayList<>();
                    List<Integer> reasonCellIndices = new ArrayList<>();
                    List<Integer> toPurpleCellIndices = new ArrayList<>();
                    for (int k = 0; k < 9; k++) {
                        ClassicCell cell = boxCells.get(k);
                        if (!cell.isSet() && (currentGrid.getRowIndex(currentGrid.getIndexByBoxIndex(box, k)) != rowIndex || !cell.getPossibleValues().contains(n))) {
                            reasonCellIndices.add(currentGrid.getIndexByBoxIndex(box, k));
                        }
                        else if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                            pointingCellIndices.add(currentGrid.getIndexByBoxIndex(box, k));
                        }
                        else toPurpleCellIndices.add(currentGrid.getIndexByBoxIndex(box, k));
                    }
                        
                    for (int rowCellIndex=0; rowCellIndex < 9; rowCellIndex++) {
                        int cellIndex = currentGrid.getIndexByRowIndex(rowIndex, rowCellIndex);
                        if (!pointingCellIndices.contains(cellIndex) && !currentGrid.get(cellIndex).isSet()) {
                            boolean r = bufferGrid.get(cellIndex).removePossibleValue(n);
                            if (r) {
                                removePointingOperation.addStep(new RemovePossibleInPointingStep<>(cellIndex, n, reasonCellIndices, pointingCellIndices, toPurpleCellIndices));
                            }
                        }
                    }
                }
            }
        }

        // check single column cell possible in box
        for (int box = 0; box < 9; box++) {
            List<ClassicCell> boxCells = currentGrid.getBox(box);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possibleColumnPlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = boxCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possibleColumnPlaces.add(currentGrid.getColumnIndex(currentGrid.getIndexByBoxIndex(box, k)));
                    }
                }
                if (possibleColumnPlaces.size() == 1) {
                    int columnIndex = possibleColumnPlaces.iterator().next();

                    List<Integer> pointingCellIndices = new ArrayList<>();
                    List<Integer> reasonCellIndices = new ArrayList<>();
                    List<Integer> toPurpleCellIndices = new ArrayList<>();
                    for (int k = 0; k < 9; k++) {
                        ClassicCell cell = boxCells.get(k);
                        if (!cell.isSet() && (currentGrid.getColumnIndex(currentGrid.getIndexByBoxIndex(box, k)) != columnIndex || !cell.getPossibleValues().contains(n))) {
                            reasonCellIndices.add(currentGrid.getIndexByBoxIndex(box, k));
                        }
                        else if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                            pointingCellIndices.add(currentGrid.getIndexByBoxIndex(box, k));
                        }
                        else toPurpleCellIndices.add(currentGrid.getIndexByBoxIndex(box, k));
                    }
                        
                    for (int columnCellIndex=0; columnCellIndex < 9; columnCellIndex++) {
                        int cellIndex = currentGrid.getIndexByColumnIndex(columnIndex, columnCellIndex);
                        if (!pointingCellIndices.contains(cellIndex) && !currentGrid.get(cellIndex).isSet()) {
                            boolean r = bufferGrid.get(cellIndex).removePossibleValue(n);
                            if (r) {
                                removePointingOperation.addStep(new RemovePossibleInPointingStep<>(cellIndex, n, reasonCellIndices, pointingCellIndices, toPurpleCellIndices));
                            }
                        }
                    }
                }
            }
        }


    
        removePointingOperation.completeInitialization();
        if (removePointingOperation.isDoingNothing()) {
            return false;
        }
        history.addOperation(removePointingOperation);
        return true;
    }        

    private boolean placeSingles() {
        System.out.println("Placing singles...");

        PlaceSinglesOperation<ClassicGrid, ClassicCell> placeSinglesOperation = new PlaceSinglesOperation<>(history.getNextOperationID());    
        
        // check rows
        for (int row = 0; row < 9; row++) {
            List<ClassicCell> rowCells = currentGrid.getRow(row);
            List<ClassicCell> bufferRowCells = bufferGrid.getRow(row);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possiblePlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = rowCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possiblePlaces.add(k);
                    }
                }
                if (possiblePlaces.size() == 1) {
                    int cellIndex = possiblePlaces.iterator().next();
                    bufferRowCells.get(cellIndex).setValue(n);
                    List<Integer> emptyIndices = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        ClassicCell c = bufferRowCells.get(i);
                        if (!c.isSet()) {
                            emptyIndices.add(currentGrid.getIndexByRowIndex(row, i));
                        }
                    }
                    placeSinglesOperation.addStep(new SingleInStep<>(currentGrid.getIndexByRowIndex(row, cellIndex), n, emptyIndices));
                }
            }
            
        }

        // check columns
        for (int column = 0; column < 9; column++) {
            List<ClassicCell> columnCells = currentGrid.getColumn(column);
            List<ClassicCell> bufferColumnCells = bufferGrid.getColumn(column);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possiblePlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = columnCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possiblePlaces.add(k);
                    }
                }
                if (possiblePlaces.size() == 1) {
                    int cellIndex = possiblePlaces.iterator().next();
                    bufferColumnCells.get(cellIndex).setValue(n);
                    List<Integer> emptyIndices = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        ClassicCell c = bufferColumnCells.get(i);
                        if (!c.isSet()) {
                            emptyIndices.add(currentGrid.getIndexByColumnIndex(column, i));
                        }
                    }
                    placeSinglesOperation.addStep(new SingleInStep<>(currentGrid.getIndexByColumnIndex(column, cellIndex), n, emptyIndices));
                }
            }
        }

        // check boxes
        for (int box = 0; box < 9; box++) {
            List<ClassicCell> boxCells = currentGrid.getBox(box);
            List<ClassicCell> bufferBoxCells = bufferGrid.getBox(box);
            for (int n = 0; n < 9; n++) {
                Set<Integer> possiblePlaces = new HashSet<>();
                for (int k = 0; k < 9; k++) {
                    ClassicCell cell = boxCells.get(k);
                    if (!cell.isSet() && cell.getPossibleValues().contains(n)) {
                        possiblePlaces.add(k);
                    }
                }
                if (possiblePlaces.size() == 1) {
                    int cellIndex = possiblePlaces.iterator().next();
                    bufferBoxCells.get(cellIndex).setValue(n);
                    List<Integer> emptyIndices = new ArrayList<>();
                    for (int i = 0; i < 9; i++) {
                        ClassicCell c = bufferBoxCells.get(i);
                        if (!c.isSet()) {
                            emptyIndices.add(currentGrid.getIndexByBoxIndex(box, i));
                        }
                    }
                    placeSinglesOperation.addStep(new SingleInStep<>(currentGrid.getIndexByBoxIndex(box, cellIndex), n, emptyIndices));
                }
            }
        }

        placeSinglesOperation.completeInitialization();
        if (placeSinglesOperation.isDoingNothing()) {
            return false;
        }
        history.addOperation(placeSinglesOperation);
        return true;
    }

    private boolean placeObvious() {
        System.out.println("Placing obvious values...");

        PlaceObviousOperation<ClassicGrid, ClassicCell> placeObviousOperation = new PlaceObviousOperation<>(history.getNextOperationID());

        for (int i = 0; i < 81; i++) {
            ClassicCell cell = currentGrid.get(i);
            if (cell.isSet()) {
                continue;
            }
            Set<Integer> possibles = cell.getPossibleValues();
            if (possibles.size() == 1) {
                Integer value = possibles.iterator().next();
                bufferGrid.get(i).setValue(value);
                placeObviousOperation.addStep(new PlaceObviousStep<>(i, value));
            }
        }

        placeObviousOperation.completeInitialization();
        if (placeObviousOperation.isDoingNothing()) {
            return false;
        }
        history.addOperation(placeObviousOperation);
        return true;
    }

    private boolean computePossibles() {
        System.out.println("Computing possibles...");

        ComputePossiblesOperation<ClassicGrid, ClassicCell> computePossiblesOperation = new ComputePossiblesOperation<>(history.getNextOperationID());

        // check rows
        for (int i = 0; i < 9; i++) {
            List<ClassicCell> row = currentGrid.getRow(i);
            Integer[] values = new Integer[9];
            Set<Integer> occupied = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (row.get(j).isSet()) {
                    values[row.get(j).getValue()] = j;
                    occupied.add(row.get(j).getValue());
                }
            }

            for (int j = 0; j < 9; j++) {
                if (!row.get(j).isSet()) {
                    for (Integer v: occupied) {
                        boolean r = row.get(j).removePossibleValue(v);
                        if (r) {
                            computePossiblesOperation.addStep(new RemovePossibleStep<>(currentGrid.getIndexByRowIndex(i, j), v, currentGrid.getIndexByRowIndex(i, values[v])));
                        }
                    }
                }
            }
        }

        // check columns
        for (int i = 0; i < 9; i++) {
            List<ClassicCell> column = currentGrid.getColumn(i);
            Integer[] values = new Integer[9];
            Set<Integer> occupied = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (column.get(j).isSet()) {
                    values[column.get(j).getValue()] = j;
                    occupied.add(column.get(j).getValue());
                }
            }

            for (int j = 0; j < 9; j++) {
                if (!column.get(j).isSet()) {
                    for (Integer v: occupied) {
                        boolean r = column.get(j).removePossibleValue(v);
                        if (r) {
                            computePossiblesOperation.addStep(new RemovePossibleStep<>(currentGrid.getIndexByColumnIndex(i, j), v, currentGrid.getIndexByColumnIndex(i, values[v])));
                        }
                    }
                }
            }
        }

        // check boxes
        for (int i = 0; i < 9; i++) {
            List<ClassicCell> box = currentGrid.getBox(i);
            Integer[] values = new Integer[9];
            Set<Integer> occupied = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (box.get(j).isSet()) {
                    values[box.get(j).getValue()] = j;
                    occupied.add(box.get(j).getValue());
                }
            }

            for (int j = 0; j < 9; j++) {
                if (!box.get(j).isSet()) {
                    for (Integer v: occupied) {
                        boolean r = box.get(j).removePossibleValue(v);
                        if (r) {
                            computePossiblesOperation.addStep(new RemovePossibleStep<>(currentGrid.getIndexByBoxIndex(i, j), v, currentGrid.getIndexByBoxIndex(i, values[v])));
                        }
                    }
                }
            }
        }

        computePossiblesOperation.completeInitialization();
        if (computePossiblesOperation.isDoingNothing()) {
            return false;
        }
        history.addOperation(computePossiblesOperation);
        return true;
    }
    
}
