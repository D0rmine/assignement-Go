import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;

import java.lang.ref.WeakReference;

//class definition for a custom reversi control
class CustomControl extends Control {
    // private fields of a reversi board
    private final GoBoard goBoard;

    // constructor for the class
    public CustomControl(Hud hud) {
        setSkin(new CustomControlSkin(this));
        goBoard = new GoBoard(hud);

        //place a piece when a player click in the board
        setOnMousePressed(event -> goBoard.placePiece(event.getSceneX() - Hud.HUD_WIDTH, event.getSceneY()));

        hud.setGoBoardWeakReference(new WeakReference<>(goBoard));

        //add goBoard to the control
        getChildren().add(goBoard);
    }

    // overridden version of the resize method
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        //resize the board
        goBoard.resize(width, height);
    }
}