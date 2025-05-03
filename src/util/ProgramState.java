package util;

import states.ClassicSudokuMenu;
import states.AboutMenu;
import states.AbstractState;
import states.MainMenu;
import states.TypeSelectionMenu;

public enum ProgramState {
    MENU(new MainMenu()), 
    ABOUT(new AboutMenu()),
    CLIPBOARD_TYPE_SELECTION(new TypeSelectionMenu()),
    CLASSIC_SUDOKU(new ClassicSudokuMenu()),;

    public static ProgramState currentState = ProgramState.MENU;
    private AbstractState state;

    ProgramState(AbstractState state) {
        this.state = state;
    }

    public AbstractState getState() {
        return state;
    }
}
