package sudokues;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grids.ClassicGrid;
import grids.cells.ClassicCell;
import operations.ComputePossiblesOperation;
import operations.PlaceObviousOperation;
import operations.steps.PlaceObviousStep;
import operations.steps.RemovePossibleStep;

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
            boolean flag = false;
            flag |= computePossibles();
            flag |= placeObvious();

            if (!flag) {
                break;
            }
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


    private boolean placeObvious() {
        System.out.println("Placing obvious values...");

        PlaceObviousOperation<ClassicGrid, ClassicCell> placeObviousOperation = new PlaceObviousOperation<>(history.getNextOperationID());

        for (int i = 0; i < 81; i++) {
            ClassicCell cell = currentGrid.get(i);
            if (cell.isSet()) {
                continue;
            }
            Set<Integer> possibles = cell.getValues();
            if (possibles.size() == 1) {
                Integer value = possibles.iterator().next();
                cell.setValue(value);
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
