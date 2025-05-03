package grids;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import grids.cells.ClassicCell;
import grids.rules.Box;
import grids.rules.Column;
import grids.rules.Row;

public class ClassicGrid extends AbstractGrid<ClassicGrid, ClassicCell> implements Row<ClassicCell>, Box<ClassicCell>, Column<ClassicCell> {
    private List<List<Integer>> rowIndices;
    private List<List<Integer>> columnIndices;
    private List<List<Integer>> boxIndices;

    private ClassicGrid(List<ClassicCell> grid, boolean t) {
        super(grid);

        this.rowIndices = new ArrayList<>();
        for (int i = 0; i < 81; i += 9) {
            List<Integer> row = new ArrayList<>();
            for (int k = i; k < i + 9; k++) {
                row.add(k);
            }
            this.rowIndices.add(row);
        }

        this.columnIndices = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<Integer> column = new ArrayList<>();
            for (int k = i; k < 81; k += 9) {
                column.add(k);
            }
            this.columnIndices.add(column);
        }

        this.boxIndices = new ArrayList<>();
        for (int rowStart = 0; rowStart < 81; rowStart += 27) {
            for (int boxStart = 0; boxStart < 9; boxStart += 3) {
                List<Integer> box = new ArrayList<>();
                for (int rowOffset = 0; rowOffset < 27; rowOffset += 9) {
                    for (int colOffset = 0; colOffset < 3; colOffset++) {
                        box.add(rowStart + boxStart + rowOffset + colOffset);
                    }
                }
                this.boxIndices.add(box);
            }
        }
    }

    public ClassicGrid(List<Integer> grid) {
        this(createCells(grid), true);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 81; i += 9) {
            sb.append(" ");
            for (int k = i; k < i + 9; k++) {
                sb.append(baseGrid.get(k));
                if ((k % 9) % 3 == 2 && k % 9 != 8) {
                    sb.append(" |");
                }
                sb.append(" ");
            }
            sb.setLength(sb.length() - 1); // Remove the last space
            sb.append("\n");
            if ((i / 9) % 3 == 2 && i != 72) {
                sb.append("-------+-------+-------\n");
            }
        }
        return sb.toString();
    }

    private static List<ClassicCell> createCells(List<Integer> grid) {
        List<ClassicCell> cells = new ArrayList<>();
        for (Integer i : grid) {
            if (i == 0) {
                cells.add(new ClassicCell());
            } else {
                cells.add(new ClassicCell(i - 1));
            }
        }
        return cells;
    }

    @Override
    public ClassicGrid copy() {
        ArrayList<ClassicCell> newGrid = new ArrayList<>();
        for (ClassicCell cell : this.baseGrid) {
            newGrid.add(cell.copy());
        }
        return new ClassicGrid(newGrid, true);
    }

    @Override
    public int getColumnIndex(int cellIndex) {
        return cellIndex % 9;
    }

    @Override
    public int getRowIndex(int cellIndex) {
        return cellIndex / 9;
    }

    @Override
    public int getBoxIndex(int cellIndex) {
        return (cellIndex / 27) * 3 + (cellIndex % 9) / 3;
    }

    @Override
    public boolean isValid() {
        // Check columns
        for (int i = 0; i < 9; i++) {
            List<ClassicCell> column = getColumn(i);
            if (!isValidGroup(column)) {
                return false;
            }
        }

        // Check rows
        for (int i = 0; i < 9; i++) {
            List<ClassicCell> row = getRow(i);
            if (!isValidGroup(row)) {
                return false;
            }
        }

        // Check squares
        for (int i = 0; i < 9; i++) {
            List<ClassicCell> square = getBox(i);
            if (!isValidGroup(square)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidGroup(List<ClassicCell> group) {
        Set<Integer> values = new HashSet<>();
        Set<Integer> availableValues = new HashSet<>();

        for (ClassicCell cell : group) {
            if (cell.isSet()) {
                if (!values.add(cell.getValue())) {
                    return false; // Duplicate value found
                }
            }
            else {
                availableValues.addAll(cell.getValues());
            }
        }

        // Check if all possible values are covered
        availableValues.addAll(values);
        return availableValues.size() == 9;
    }

    @Override
    public List<ClassicCell> getColumn(int columnIndex) {
        return columnIndices.get(columnIndex).stream()
                .map(index -> baseGrid.get(index))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassicCell> getRow(int rowIndex) {
        return rowIndices.get(rowIndex).stream()
                .map(index -> baseGrid.get(index))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClassicCell> getBox(int squareIndex) {
        return boxIndices.get(squareIndex).stream()
                .map(index -> baseGrid.get(index))
                .collect(Collectors.toList());
    }
}
