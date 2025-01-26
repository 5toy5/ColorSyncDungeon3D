package toyoura.game.engine;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;
import javafx.scene.PerspectiveCamera;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestApp extends Application {

    static class Snowman {
        Group group;

        public Snowman(double baseRadius, double topRadius, double offsetY) {
            Sphere base = new Sphere(baseRadius);
            Sphere top = new Sphere(topRadius);
            top.getTransforms().add(new Translate(0, 0, -17)); // Z軸方向に積み上げ

            Sphere eye1 = new Sphere(1);
            eye1.setMaterial(new javafx.scene.paint.PhongMaterial(Color.BLACK));
            eye1.getTransforms().add(new Translate(-2, -6, -17)); // Y軸方向に位置を調整

            Sphere eye2 = new Sphere(1);
            eye2.setMaterial(new javafx.scene.paint.PhongMaterial(Color.BLACK));
            eye2.getTransforms().add(new Translate(2, -6, -17)); // Y軸方向に位置を調整

            Cylinder nose = new Cylinder(0.5, 5);
            nose.setMaterial(new javafx.scene.paint.PhongMaterial(Color.ORANGE));
            nose.getTransforms().add(new Translate(0, -4, -20)); // Y軸方向に位置を調整

            group = new Group(base, top, eye1, eye2, nose);
            group.getTransforms().add(new Translate(0, offsetY, 0)); // 位置を調整
        }

        public Group getGroup() {
            return group;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("こんにちは");
        System.out.println("Hello");
        Group root = new Group();

        Snowman snowman1 = new Snowman(10, 7, 0);
        Snowman snowman2 = new Snowman(10, 7, 50);

        // 黒色の板定義
        Box blackBoard = new Box(200, 200, 1);
        blackBoard.setTranslateZ(0); // Z=0の地点に配置
        blackBoard.setMaterial(new javafx.scene.paint.PhongMaterial(Color.BLACK));

        // Groupに追加
        root.getChildren().addAll(blackBoard, snowman1.getGroup(), snowman2.getGroup());

        // カメラ定義
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(45.0);
        camera.setFarClip(200);
        camera.getTransforms().addAll(
                new Translate(0, 0, -180)
        );

        final Scene scene = new Scene(root, 800, 600, true);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.setTitle("JavaFX 3D Snowmen");
        stage.show();

        // Timeline定義
        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    double currentTime = (System.currentTimeMillis() % 10000) / 10000.0;
                    double newX = 100 * Math.sin(currentTime * 2 * Math.PI);
                    snowman1.getGroup().getTransforms().setAll(new Translate(newX, 0, 0));
                })
        );

        Timeline timeline2 = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    double currentTime = (System.currentTimeMillis() % 10000) / 10000.0;
                    double newY = 50 * Math.cos(currentTime * 2 * Math.PI);
                    snowman2.getGroup().getTransforms().setAll(new Translate(0, 50 + newY, 0));
                })
        );

        timeline1.setCycleCount(Timeline.INDEFINITE);
        timeline1.play();

        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
