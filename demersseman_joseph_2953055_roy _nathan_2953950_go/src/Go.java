//an experiment to see how much JavaFX code is required
//to build a game of reversi

//imports

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

//class definition for reversi game
public class Go extends Application {
    // private fields for a stack pane and a reversi control
    private StackPane stackPaneMainLayout;
    private CustomControl goControl;
    private HBox mainHBox;

    // entry point into our program for launching our javafx application
    public static void main(String[] args) {
        launch(args);
    }

    // overridden init method
    @Override
    public void init() throws Exception {
        super.init();

        mainHBox = new HBox();
        stackPaneMainLayout = new StackPane();

        Hud hud = new Hud();
        hud.setMinWidth(Hud.HUD_WIDTH);

        goControl = new CustomControl(hud);
        stackPaneMainLayout.getChildren().add(goControl);

        mainHBox.getChildren().addAll(hud,stackPaneMainLayout);

        HBox.setHgrow(stackPaneMainLayout, Priority.ALWAYS);
    }

    // overridden start method
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GO");
        primaryStage.setScene(new Scene(mainHBox, 1000+Hud.HUD_WIDTH, 1000));
        primaryStage.show();
    }


}
