import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.lang.ref.WeakReference;

public class Hud extends VBox {

    public static final int HUD_WIDTH = 150;
    //the time for a player before passing his turn; in second
    public static final int TIME_FOR_ONE_PLAYER = 30;

    //weak reference, to not store the board fully but only the reference to it
    private WeakReference<GoBoard> goBoardWeakReference;

    // current player
    private Ellipse playerEllipse;

    // labels to see the Score of all players
    private Label playerOneScoreLabel;
    private Label playerTwoScoreLabel;

    private Task playerTimer;
    private ProgressIndicator progressIndicator;

    public Hud() {

        Label playerLabel = new Label("Turn : ");
        //set Margin for putting space between label and ellipse
        setMargin(playerLabel, new Insets(0, 0, 20, 0));

        StackPane panePlayerTurn = new StackPane();

        panePlayerTurn.setStyle("-fx-background-color: #FFFFFF;");

        //Ellipse who symbolize the current player
        playerEllipse = new Ellipse();
        playerEllipse.setRadiusX(25);
        playerEllipse.setRadiusY(25);
        progressIndicator = new ProgressIndicator(0);

        progressIndicator.setPrefSize( 125, 125 );

        progressIndicator.setAccessibleText("");
        panePlayerTurn.getChildren().add(progressIndicator);
        panePlayerTurn.getChildren().add(playerEllipse);
        StackPane.setAlignment(playerEllipse, Pos.CENTER);
        progressIndicator.setCenterShape(true);

        Label player1ScoreText = new Label("Player 1 : ");
        //set Margin for putting space between ellipse and label (for the top) and label and label (for the bottom)
        setMargin(player1ScoreText, new Insets(50, 0, 20, 0));
        playerOneScoreLabel = new Label();
        //set textColor with the color of the player; player 1 = White
        playerOneScoreLabel.setTextFill(Color.WHITE);

        Label player2ScoreText = new Label("Player 2 : ");
        //set Margin for putting space between labels
        setMargin(player2ScoreText, new Insets(30, 0, 20, 0));
        playerTwoScoreLabel = new Label();
        //set textColor with the color of the player; player 2 = Black
        playerTwoScoreLabel.setTextFill(Color.BLACK);
        //put some space between labels and button
        setMargin(playerTwoScoreLabel, new Insets(20, 0, 50, 0));
        Button passButton = new Button("Pass your turn");

        passButton.setOnAction((ActionEvent event) -> {
            passTurn();
        });

        Button restartButton = new Button("Restart");
        //put some space between passButton and restartButton, restartButton and rulesButton
        setMargin(restartButton, new Insets(20, 0, 20, 0));
        restartButton.setOnAction(event -> {
            if (goBoardWeakReference.get() != null) {
                goBoardWeakReference.get().resetGame();
            }
        });

        Button rulesButton = new Button("Rules");
        rulesButton.setOnAction((ActionEvent event) -> {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rules Dialog");
            alert.initStyle(StageStyle.UTILITY);
            alert.setHeaderText(null);
            alert.setResizable(true);
            alert.getDialogPane().setPrefSize(800, 600);

            ScrollPane scrollPane = new ScrollPane();

            Text rules = new Text("Begin\n" +
                    "- Players: Go is a game between two players, called Black and White.\n- Board: Go is played on a plain grid, called a board.\n" +
                    "- Stones: Go is played with playing tokens known as stones. Each player has at their disposal stones of the same color.\n" +
                    "- Positions: At any time in the game, each intersection on the board is in one and only one of the following three states: 1) empty; 2) occupied by a black stone; or 3) occupied by a white stone. A position consists of an indication of the state of each intersection.\n" +
                    "\nPlay\n" +
                    "- Initial position: At the beginning of the game, the board is empty.\n" +
                    "- Turns: Black moves first. The players alternate thereafter.\n" +
                    "- Moving: When it is their turn, a player may either pass or play. A play consists of the following steps (performed in the prescribed order):\n" +
                    "\tStep 1. (Playing a stone) Placing a stone of their color on an empty intersection. It can never be moved to another intersection after being played.\n" +
                    "\tStep 2. (Capture) Removing from the board any stones of their opponent's color that have no liberties.\n" +
                    "\tStep 3. (Self-capture) Removing from the board any stones of their own color that have no liberties.\n" +
                    "\tProhibition of suicide: A play is illegal if one or more stones of that player's color would be removed in Step 3 of that play.\n" +
                    "- Prohibition of repetition: A play is illegal if it would have the effect (after all steps of the play have been completed) of creating a position that has occurred previously in the game.\n" +
                    "\nEnd\n" +
                    "- End: The game ends when both players have passed consecutively. The final position is the position on the board at the time the players pass consecutively.\n" +
                    "\tTerritory: In the final position, an empty intersection is said to belong to a player's territory if all stones adjacent to it or to an empty intersection connected to it are of that player's color.\n" +
                    "\tArea: In the final position, an intersection is said to belong to a player's area if either: 1) it belongs to that player's territory; or 2) it is occupied by a stone of that player's color.\n" +
                    "\tScore: A player's score is the number of intersections in their area in the final position.\n" +
                    "- Winner: If one player has a higher score than the other, then that player wins. Otherwise, the game is drawn.");
            scrollPane.setContent(rules);
            scrollPane.setFitToHeight(true);
            alert.getDialogPane().setContent(scrollPane);
            alert.showAndWait();
        });


        //label to list the bonuses
       /* Label labelBonus = new Label("Bonuses :\n- HUD");
        labelBonus.setPadding(new Insets(50, 0, 0, 0));
        labelBonus.setFont(new Font("Arial", 13));
*/
        //add all nodes to the HUD
        getChildren().addAll(playerLabel, panePlayerTurn, player1ScoreText, playerOneScoreLabel, player2ScoreText, playerTwoScoreLabel, passButton, restartButton, rulesButton);

        //set alignment to center to be more beautiful
        setAlignment(Pos.CENTER);


        //set background texture of the HUD
        setBackground(new Background(new BackgroundImage(Sprites.BACKGROUND_HUD.getImage(), null, null, null, null)));
    }

    private void resetTimer() {
        if (playerTimer != null && playerTimer.isRunning())
            playerTimer.cancel(true);
        playerTimer = timerBeforePassTurn();

        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(playerTimer.progressProperty());

        new Thread(playerTimer).start();
    }

    private void passTurn() {
        if (goBoardWeakReference.get() != null) {
            goBoardWeakReference.get().passTurn();
        }
    }

    public void setPlayerScore(int playerOneScore, int playerTwoScore) {
        playerOneScoreLabel.setText(String.valueOf(playerOneScore));
        playerTwoScoreLabel.setText(String.valueOf(playerTwoScore));
    }

    public void setCurrentPlayerTurn(int currentPlayer, boolean play) {
        if (play) {
            setEllipseColor(currentPlayer);
            resetTimer();
        }
        else
        {
            playerTimer.cancel(true);
        }
    }

    private void setEllipseColor(int currentPlayer) {
        playerEllipse.setFill((currentPlayer == 1) ? Sprites.PLAYER_1 : Sprites.PLAYER_2);
    }

    public void setGoBoardWeakReference(WeakReference<GoBoard> goBoardWeakReference) {
        this.goBoardWeakReference = goBoardWeakReference;
    }

    private Task timerBeforePassTurn() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(TIME_FOR_ONE_PLAYER * 10);
                    updateProgress(i + 1, 100);
                }
                return true;
            }
        };
    }


    public void setProgressIndicatorProperty() {
        progressIndicator.progressProperty().addListener((observable, oldValue, newValue) -> {

            Text text = (Text) progressIndicator.lookup(".percentage");
            if (text != null) {
                text.setText("");
                progressIndicator.setPrefWidth(text.getLayoutBounds().getWidth());
                progressIndicator.setPadding(new Insets(text.getLayoutBounds().getHeight(),0,0,0));
            }
            if (newValue.doubleValue() >= 1) {
                passTurn();
            }
        });
    }

}
