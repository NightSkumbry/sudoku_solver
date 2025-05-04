package sudokues;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import grids.ClassicGrid;
import grids.cells.ClassicCell;
import operations.ComputePossiblesOperation;
import operations.steps.RemovePossibleStep;

public class ClassicSudoku extends AbstractSudoku<ClassicGrid, ClassicCell> {
    public ClassicSudoku(List<Integer> grid) {
        super(new ClassicGrid(grid));
    }

    @Override
    public void findOneSolution() {
        System.out.println("Finding one solution...");

        computePossibles();
    }

    @Override
    public void findAllSolutions() {
        while (!fullySolved) {
            findOneSolution();
            break; //TODO: remove this line when implementing findOneSolution
        }
    }

    private void computePossibles() {
        System.out.println("Computing possibles...");

        ComputePossiblesOperation<ClassicGrid, ClassicCell> computePossiblesOperation = new ComputePossiblesOperation<>();

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
        history.addOperation(computePossiblesOperation);

    }
    
}
