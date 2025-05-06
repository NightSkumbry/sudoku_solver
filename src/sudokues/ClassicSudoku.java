package sudokues;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grids.ClassicGrid;
import grids.cells.ClassicCell;
import operations.ComputePossiblesOperation;
import operations.PlaceObviousOperation;
import operations.PlaceSinglesOperation;
import operations.steps.PlaceObviousStep;
import operations.steps.RemovePossibleStep;
import operations.steps.SingleInStep;

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
            computePossibles();
            this.bufferGrid = currentGrid.copy();
            
            if (placeObvious()) continue;

            if (placeSingles()) continue;

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
                        ClassicCell c = rowCells.get(i);
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
                        ClassicCell c = columnCells.get(i);
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
                        ClassicCell c = boxCells.get(i);
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
