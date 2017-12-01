import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import javax.swing.*;

public class Sprites {
    // Background sprites
    public static final ImagePattern BACKGROUND = new ImagePattern(new Image("file:sprites/background.png"));
    public static final ImagePattern BACKGROUND_HUD = new ImagePattern(new Image("file:sprites/background_hud.png"));

    // Player sprites
    public static final ImagePattern PLAYER_1 = new ImagePattern(new Image("file:sprites/player_1.png"));
    public static final ImagePattern PLAYER_2 = new ImagePattern(new Image("file:sprites/player_2.png"));
}
