//an experiment to see how much JavaFX code is required
//to build a game of reversi

//imports

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

//class definition for reversi game
public class Go extends Application {
    // private fields for a stack pane and a reversi control
    private StackPane stackPaneMainLayout;
    private CustomControl goControl;
    private HBox mainHBox;
    private Hud hud;

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

        hud = new Hud();
        hud.setMinWidth(Hud.HUD_WIDTH);

        goControl = new CustomControl(hud);
        stackPaneMainLayout.getChildren().add(goControl);

        mainHBox.getChildren().addAll(hud, stackPaneMainLayout);

        HBox.setHgrow(stackPaneMainLayout, Priority.ALWAYS);
    }

    // overridden start method
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GO");
        primaryStage.setScene(new Scene(mainHBox, 1000 + Hud.HUD_WIDTH, 1000));
        primaryStage.show();
        hud.setProgressIndicatorProperty();
    }
}
