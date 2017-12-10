import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;


//class definition for the reversi board
class GoBoard extends Pane {

    public static final int NUMBER_OF_LINE = 7;
    private static final int LABEL_SIZE = 16;

    private final Label[][] labels;
    private final Translate[][] labelTranslate;

    // arrays for the lines that makeup the horizontal and vertical grid lines
    private final Line[] horizontal;
    private final Line[] vertical;
    // arrays holding translate objects for the horizontal and vertical grid lines
    private final Translate[] horizontal_t;
    private final Translate[] vertical_t;
    // arrays for the internal representation of the board and the pieces that are
    // in place
    private final Stone[][] render;
    // rectangle that makes the background of the board
    private Rectangle background;
    // the current player who is playing and who is his opposition
    private int current_player;
    private int opposing;
    // is the game currently in play
    private boolean in_play;
    // current scores of player 1 and player 2
    private int player1_score;
    private int player2_score;
    // the width and height of a cell in the board
    private double cell_width;
    private double cell_height;
    private int passConsecutive = 0;

    private GameLogic gameLogic;
    private Hud hud;

    // default constructor for the class
    public GoBoard(Hud hud) {
        passConsecutive = 0;
        render = new Stone[NUMBER_OF_LINE][NUMBER_OF_LINE];
        horizontal = new Line[NUMBER_OF_LINE];
        vertical = new Line[NUMBER_OF_LINE];
        horizontal_t = new Translate[NUMBER_OF_LINE];
        vertical_t = new Translate[NUMBER_OF_LINE];
        labels = new Label[4][NUMBER_OF_LINE];
        labelTranslate = new Translate[4][NUMBER_OF_LINE];
        gameLogic = new GameLogic(this);

        this.hud = hud;

        initialiseLinesBackground();
        initialiseRender();
        initLabels();
        resetGame();
    }


    // public method that will try to place a piece in the given x,y coordinate
    public void placePiece(final double x, final double y) {

        //if the party is not started, don't continue
        if (!in_play)
            return;
        final int cx = (int) Math.round(((x - 2 * cell_width) / cell_width));
        final int cy = (int) Math.round((y - 2 * cell_height) / cell_height);

        if (getPiece(cx, cy) == -1 || getPiece(cx, cy) == 1 || getPiece(cx, cy) == 2)
            return;
        render[cx][cy].setPiece(current_player);

        gameLogic.captureOpposingPiece(cx, cy);

        //check if the piece have an escape
        if (!gameLogic.sucideRule(cx, cy, current_player)) {
            render[cx][cy].setPiece(0);
            return;
        }

        if (current_player == 0) {
            if (!gameLogic.CheckKORule(render, current_player, player1_score))
                swapPlayers();
        } else {
            if (!gameLogic.CheckKORule(render, current_player, player2_score))
                swapPlayers();
        }
        hud.setPlayerScore(player1_score, player2_score);
        passConsecutive = 0;
    }

    public void passTurn() {
        if (current_player == 1)
            player2_score++;
        if (current_player == 2)
            player1_score++;
        hud.setPlayerScore(player1_score, player2_score);
        passConsecutive++;
        if (passConsecutive >= 2)
            EndOfTheGame();
        swapPlayers();
    }

    private void EndOfTheGame() {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                if (render[i][j].getPiece() == 1 || render[i][j].getPiece() == 2) {
                    gameLogic.CheckTeritoryFromPiece(i, j);
                }
            }
        }
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                if (render[i][j].getPiece() == 3)
                    player1_score++;
                if (render[i][j].getPiece() == 4)
                    player2_score++;
            }
        }
        hud.setPlayerScore(player1_score, player2_score);
        if (player1_score > player2_score)
            System.out.println("White win");
        else if (player2_score > player1_score)
            System.out.println("Black win");
        else
            System.out.println("Equality");
        in_play = false;
    }

    // overridden version of the resize method to give the board the correct size
    @Override
    public void resize(double width, double height) {
        super.resize(width, height);

        cell_width = Math.round(width / (NUMBER_OF_LINE + 3));
        cell_height = Math.round(height / (NUMBER_OF_LINE + 3));

        //set the new dimensions of the Background rectangle
        background.setWidth(width);
        background.setHeight(height);

        //resize and relocate the lines
        horizontalResizeRelocate();
        verticalResizeRelocate();

        //resize and relocate the pieces
        pieceResizeRelocate();
        labelResizeRelocate();
    }

    // public method for resetting the game
    public void resetGame() {
        resetRenders();
        in_play = true;
        current_player = 2;
        opposing = 1;
        player1_score = 0;
        player2_score = 0;
        hud.setPlayerScore(player1_score, player2_score);
        hud.setCurrentPlayerTurn(current_player, in_play);
    }

    // private method that will reset the renders
    private void resetRenders() {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                render[i][j].setPiece(0);
            }
        }
    }

    public void setRender(Stone[][] newRender) {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                render[i][j].setPiece(newRender[i][j].getPiece());
            }
        }
    }

    public void setScore(int newScore, int player) {
        if (player == 0)
            player1_score = newScore;
        if (player == 1)
            player2_score = newScore;

        hud.setPlayerScore(player1_score, player2_score);
    }

    // private method that will initialise the background and the lines
    private void initialiseLinesBackground() {

        background = new Rectangle(800, 800, Sprites.BACKGROUND);

        getChildren().add(background);
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            horizontal[i] = new Line();
            horizontal_t[i] = new Translate(0, 0);
            vertical[i] = new Line();
            vertical_t[i] = new Translate(0, 0);

            horizontal[i].getTransforms().add(horizontal_t[i]);
            vertical[i].getTransforms().add(vertical_t[i]);

            horizontal[i].setStartX(0);
            horizontal[i].setStartY(0);
            horizontal[i].setEndY(0);
            getChildren().add(horizontal[i]);

            vertical[i].setStartX(0);
            vertical[i].setEndX(0);
            vertical[i].setStartY(0);
            getChildren().add(vertical[i]);
        }
    }

    // private method for resizing and relocating the horizontal lines
    private void horizontalResizeRelocate() {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            horizontal[i].setStartX(2 * cell_width);
            horizontal[i].setEndX((1 + NUMBER_OF_LINE) * cell_width);
            horizontal_t[i].setY(cell_height * i + 2 * cell_height);
        }
    }

    // private method for resizing and relocating the vertical lines
    private void verticalResizeRelocate() {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            vertical[i].setStartY(2 * cell_height);
            vertical[i].setEndY((1 + NUMBER_OF_LINE) * cell_height);
            vertical_t[i].setX(cell_width * i + 2 * cell_width);
        }
    }

    // private method for swapping the players
    private void swapPlayers() {
        if (current_player == 1) {
            current_player = 2;
            opposing = 1;
        } else {
            current_player = 1;
            opposing = 2;
        }
        hud.setCurrentPlayerTurn(current_player, in_play);
    }

    // private method for resizing and relocating all the pieces
    private void pieceResizeRelocate() {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                render[i][j].resize(cell_width, cell_height);
                render[i][j].relocate((cell_width * (i + 2)) - cell_width / 2, cell_height * (j + 2) - cell_height / 2);
                //render[i][j] = new ReversiPiece(0);
            }
        }
    }

    private void labelResizeRelocate() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                if (i % 2 == 0) {
                    labelTranslate[i][j].setX((cell_width) * (j + 2) - LABEL_SIZE / 2);
                    if (i == 0)
                        labelTranslate[i][j].setY((cell_height) / 2);
                    else
                        labelTranslate[i][j].setY((cell_height) / 4 + cell_height * (NUMBER_OF_LINE + 2));
                } else {
                    if (i == 1)
                        labelTranslate[i][j].setX((cell_width) / 2);
                    else
                        labelTranslate[i][j].setX((cell_width) / 4 + cell_width * (NUMBER_OF_LINE + 2));
                    labelTranslate[i][j].setY((cell_height) * (j + 2) - LABEL_SIZE / 2);
                }
            }
        }
    }

    // private method for getting a piece on the board. this will return the board
    // value unless we access an index that doesn't exist. this is to make the code
    // for determining reverse chains much easier
    public int getPiece(final int x, final int y) {
        if (x >= NUMBER_OF_LINE || y >= NUMBER_OF_LINE || x < 0 || y < 0) {
            return -1;
        }
        return render[x][y].getPiece();
    }

    // private method that will initialise everything in the render array
    private void initialiseRender() {
        for (int i = 0; i < NUMBER_OF_LINE; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                render[i][j] = new Stone(0);
                getChildren().add(render[i][j]);
            }
        }
    }

    private void initLabels() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < NUMBER_OF_LINE; j++) {
                if (i % 2 == 0) {
                    //j + 65 because it is A in ascii form
                    labels[i][j] = new Label(String.valueOf((char) (j + 65)));
                } else {
                    //j + 1 to begin with 1
                    labels[i][j] = new Label(String.valueOf(j + 1));
                }
                labelTranslate[i][j] = new Translate();
                labels[i][j].getTransforms().add(labelTranslate[i][j]);
                labels[i][j].setMinHeight(LABEL_SIZE);
                labels[i][j].setMinWidth(LABEL_SIZE);
                getChildren().add(labels[i][j]);
            }
        }
    }

    public void changePieceIn(int x, int y, int type) {
        if (getPiece(x, y) != -1) {
            if (render[x][y].getPiece() == 1)
                player2_score++;
            else if (render[x][y].getPiece() == 2)
                player1_score++;
            render[x][y].setPiece(type);
        }
    }

    public int getCurrent_player() {
        return current_player;
    }

    public int getOpposing() {
        return opposing;
    }

}
