package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

//球体
import javafx.scene.shape.Sphere;

//位置変更
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

//カメラ
import javafx.scene.PerspectiveCamera;
import javafx.util.Duration;

public class TestApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Hello World");
        Group root = new Group();

        //球体定義
        Sphere sphere = new Sphere(10);
        //Groupに追加
        root.getChildren().add(sphere);

        //カメラ定義
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(45.0);
        camera.setFarClip(200);
        camera.getTransforms().addAll(
                new Translate(0,0,-180)
        );

        final Scene scene = new Scene(root, 800, 600, true);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.setTitle("JavaFX 3D Sample");
        stage.show();

        int number_of_reflesh = 100;
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        for(int i = 0; i < number_of_reflesh; i++){
            final int frameIndex = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.millis(100*i),
                    event -> {
                        double newRadius = 50 + 10 * Math.sin(frameIndex * 0.1);
                        sphere.setRadius(newRadius);
                    }
            );
            timeline.getKeyFrames().add(keyFrame);
        }
        timeline.play();

//        stage.setTitle("Hello World");
//        stage.setWidth(250);
//        stage.setHeight(250);
//        stage.show();
    }
}