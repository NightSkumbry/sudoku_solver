package sudokues;

import java.util.List;

import grids.ClassicGrid;
import grids.cells.ClassicCell;

public class ClassicSudoku extends AbstractSudoku<ClassicGrid, ClassicCell> {
    public ClassicSudoku(List<Integer> grid) {
        super(new ClassicGrid(grid));
    }

    @Override
    public void findOneSolution() {
        System.out.println("Finding one solution...");
    }

    @Override
    public void findAllSolutions() {
        while (!fullySolved) {
            findOneSolution();
            break; //TODO: remove this line when implementing findOneSolution
        }
    }
    
}
