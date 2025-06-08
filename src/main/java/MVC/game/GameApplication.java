package MVC.game;

import MVC.game.controller.GameController;
//import MVC.game.controller.GameControllerCamera;
import MVC.game.model3D.Snowman;
import MVC.game.view.GameView;
import MVC.game.view.GameView3D;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.animation.AnimationTimer;

import java.util.List;

public class GameApplication extends Application {
    private static final boolean USE_3D_VIEW = true; // Change this to switch between 2D and 3D views
    @Override
    public void start(Stage primaryStage) throws Exception {
        if (USE_3D_VIEW) {
            GameView3D gameView3D = new GameView3D(primaryStage);

            new GameController(null, null,primaryStage,USE_3D_VIEW);
        } else {
            Canvas canvas = new Canvas(800, 600);
            GraphicsContext gc = canvas.getGraphicsContext2D();

            StackPane root = new StackPane();
            root.getChildren().add(canvas);

            Scene scene = new Scene(root);

            new GameController(gc, scene,primaryStage,USE_3D_VIEW);

            primaryStage.setScene(scene);
            primaryStage.setTitle("MVC Game");
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}