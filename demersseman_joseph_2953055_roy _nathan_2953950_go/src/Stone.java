import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.transform.Translate;

//class definition for a reversi piece
class Stone extends Group {
    private final Ellipse piece;    // ellipse representing the player's piece
    private final Translate t;    // translation for the player piece
    // private fields
    private int player;        // the player that this piece belongs to

    // default constructor for the class
    public Stone(int player) {
        this.player = player;
        this.piece = new Ellipse();

        t = new Translate();
        piece.getTransforms().add(t);
        setEllipseColor();

        piece.setVisible((player == 1 || player == 2));
        getChildren().add(piece);
    }

    // overridden version of the resize method to give the piece the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        piece.setCenterX(width / 2);
        piece.setCenterY(height / 2);
        piece.setRadiusX(width / 2);
        piece.setRadiusY(height / 2);
    }

    // overridden version of the relocate method to position the piece correctly
    @Override
    public void relocate(double x, double y) {
        super.relocate(x, y);

        t.setX(x);
        t.setY(y);
    }

    // returns the type of this piece
    public int getPiece() {
        // NOTE: this is to keep the compiler happy until you get to this point
        return player;
    }

    // method that will set the piece type
    public void setPiece(final int type) {
        player = type;
        piece.setVisible((player == 1 || player == 2));
        setEllipseColor();
    }

    //set Ellipse Color in function of player
    private void setEllipseColor() {
        piece.setFill((player == 1) ? Color.WHITE : Color.BLACK);
    }
}
