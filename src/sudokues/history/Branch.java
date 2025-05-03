package sudokues.history;

import java.util.List;

public record Branch(int startingGridIndex, int branchOnCell, List<Integer> branchesIndex, boolean isClosed) {
    public Branch(int startingGridIndex, int branchOnCell, List<Integer> branchesIndex) {
        this(startingGridIndex, branchOnCell, branchesIndex, false);
    }
}
