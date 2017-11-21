import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;

//class definition for a custom reversi control
class CustomControl extends Control {
    // private fields of a reversi board
    private final GoBoard goBoard;

    // constructor for the class
    public CustomControl() {
        setSkin(new CustomControlSkin(this));
        goBoard = new GoBoard();

        setOnMousePressed(event -> goBoard.placePiece(event.getSceneX(), event.getSceneY()));

        getChildren().add(goBoard);
    }

    // overridden version of the resize method
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        goBoard.resize(width, height);
    }
}