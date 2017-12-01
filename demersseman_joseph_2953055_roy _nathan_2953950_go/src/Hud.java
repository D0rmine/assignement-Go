import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.lang.ref.WeakReference;

/* BONUS */

public class Hud extends VBox {

    public static final int HUD_WIDTH = 150;

    private WeakReference<GoBoard> goBoardWeakReference;

    // current player
    private Ellipse playerEllipse;

    // labels to see the Score of all players
    private Label playerOneScoreLabel;
    private Label playerTwoScoreLabel;

    public Hud() {

        Label playerLabel = new Label("Turn : ");
        //set Padding for putting space between label and ellipse
        playerLabel.setPadding(new Insets(0, 0, 20, 0));

        //Ellipse who symbolize the current player
        playerEllipse = new Ellipse();
        playerEllipse.setRadiusX(25);
        playerEllipse.setRadiusY(25);


        Label player1ScoreText = new Label("Player 1 : ");
        //set Padding for putting space between ellipse and label (for the top) and label and label (for the bottom)
        player1ScoreText.setPadding(new Insets(50, 0, 20, 0));
        playerOneScoreLabel = new Label();
        //set textColor with the color of the player; player 1 = White
        playerOneScoreLabel.setTextFill(Color.WHITE);

        Label player2ScoreText = new Label("Player 2 : ");
        //set Padding for putting space between labels
        player2ScoreText.setPadding(new Insets(30, 0, 20, 0));
        playerTwoScoreLabel = new Label();
        //set textColor with the color of the player; player 2 = Black
        playerTwoScoreLabel.setTextFill(Color.BLACK);
        //put some space between labels and button
        playerTwoScoreLabel.setPadding(new Insets(20, 0, 50, 0));
        Button passButton = new Button("Pass your turn");

        passButton.setOnAction(event -> {
            if (goBoardWeakReference.get() != null) {
                goBoardWeakReference.get().passTurn();
            }
        });
        //label to list the bonuses
       /* Label labelBonus = new Label("Bonuses :\n- HUD");
        labelBonus.setPadding(new Insets(50, 0, 0, 0));
        labelBonus.setFont(new Font("Arial", 13));
*/
        //add all of labels and the ellipse
        getChildren().addAll(playerLabel, playerEllipse, player1ScoreText, playerOneScoreLabel, player2ScoreText, playerTwoScoreLabel, passButton);

        //set alignment to center to be more beautiful
        setAlignment(Pos.CENTER);


        //set background texture of the HUD
        setBackground(new Background(new BackgroundImage(Sprites.BACKGROUND_HUD.getImage(), null, null, null, null)));
    }

    public void setPlayerScore(int playerOneScore, int playerTwoScore) {
        playerOneScoreLabel.setText(String.valueOf(playerOneScore));
        playerTwoScoreLabel.setText(String.valueOf(playerTwoScore));
    }

    public void setCurrentPlayerTurn(int currentPlayer) {
        setEllipseColor(currentPlayer);
    }

    private void setEllipseColor(int currentPlayer) {
        playerEllipse.setFill((currentPlayer == 1) ? Sprites.PLAYER_1 : Sprites.PLAYER_2);
    }

    public void setGoBoardWeakReference(WeakReference<GoBoard> goBoardWeakReference) {
        this.goBoardWeakReference = goBoardWeakReference;
    }
}
