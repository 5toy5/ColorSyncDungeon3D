package toyoura.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.PerspectiveCamera;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestApp extends Application {

    static class Snowman {
        Group group;
        Sphere top;

        public Snowman(double baseRadius, double topRadius, double offsetY) {
            Sphere base = new Sphere(baseRadius);
            top = new Sphere(topRadius);
            top.getTransforms().add(new Translate(0, 0, -17)); // Z軸方向に積み上げ

            Sphere eye1 = new Sphere(1);
            eye1.setMaterial(new PhongMaterial(Color.BLACK));
            eye1.getTransforms().add(new Translate(-2, -6, -20)); // Y軸方向に位置を調整

            Sphere eye2 = new Sphere(1);
            eye2.setMaterial(new PhongMaterial(Color.BLACK));
            eye2.getTransforms().add(new Translate(2, -6, -20)); // Y軸方向に位置を調整

            Cylinder nose = new Cylinder(0.5, 5);
            nose.setMaterial(new PhongMaterial(Color.ORANGE));
            nose.getTransforms().add(new Translate(0, -6, -17)); // Y軸方向に位置を調整

            // 左手
            Cylinder leftArm = new Cylinder(0.5, 10);
            leftArm.setMaterial(new PhongMaterial(Color.BLACK));
            leftArm.getTransforms().add(new Translate(-10, 0, -10)); // 左側に位置を調整
            leftArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS)); // 水平に配置

            // 右手
            Cylinder rightArm = new Cylinder(0.5, 10);
            rightArm.setMaterial(new PhongMaterial(Color.BLACK));
            rightArm.getTransforms().add(new Translate(10, 0, -10)); // 右側に位置を調整
            rightArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS)); // 水平に配置

            group = new Group(base, top, eye1, eye2, nose, leftArm, rightArm);
            group.getTransforms().add(new Translate(0, offsetY, 0)); // 位置を調整
        }

        public Group getGroup() {
            return group;
        }

        // 雪だるまの中心座標を取得
        public double getCenterX() {
            return group.getBoundsInParent().getCenterX();
        }

        public double getCenterY() {
            return group.getBoundsInParent().getCenterY();
        }

        public double getCenterZ() {
            return group.getBoundsInParent().getCenterZ();
        }

        public Sphere getTop() {
            return top;
        }
    }

    private double snowman1X = 0;
    private double snowman1Y = 0;
    private double snowman1Rotate = 0;
    private double cameraarg = 0;
    private Group root;
    private boolean attack = false;
    private Group attackEffectGroup = new Group();

    @Override
    public void start(Stage stage) throws Exception {
        root = new Group();

        Snowman snowman1 = new Snowman(10, 7, 0);
        Snowman snowman2 = new Snowman(10, 7, 50);

        // 板定義
        Box Board = new Box(200, 200, 1);
        Board.setTranslateZ(0); // Z=0の地点に配置
        Board.setMaterial(new PhongMaterial(Color.rgb(194, 178, 128)));

        // Groupに追加
        root.getChildren().addAll(Board, snowman1.getGroup(), snowman2.getGroup(), attackEffectGroup);

        // カメラ定義
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFieldOfView(45.0);
        camera.setFarClip(200);

        final Scene scene = new Scene(root, 800, 600, true);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.setTitle("JavaFX 3D Snowmen");
        stage.show();

        // キーイベントの追加
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W: snowman1Y -= 3; break;
                case S: snowman1Y += 3; break;
                case A: snowman1X -= 3; break;
                case D: snowman1X += 3; break;
                case E: snowman1Rotate += 3; break;
                case Q: snowman1Rotate -= 3; break;
                case DIGIT1: attack = !attack; break;
                case J: cameraarg -= 3; break;
                case K: cameraarg += 3; break;
            }
            updateSnowman1Position(snowman1, camera);
            if (attack) {
                clearAttackEffect();
            } else {
                clearAttackEffect();
            }
        });

        // Timeline定義
        Timeline timelineSnowman2 = new Timeline(
                new KeyFrame(Duration.millis(10), event -> {
                    // 雪だるま2をz軸中心に180度回転
                    snowman2.getGroup().getTransforms().setAll(new Translate(0, 0, 0));
                    snowman2.getGroup().getTransforms().add(new Rotate(180, snowman2.getCenterX(), snowman2.getCenterY(), snowman2.getCenterZ(), Rotate.Z_AXIS));
                })
        );

        timelineSnowman2.setCycleCount(Timeline.INDEFINITE);
        timelineSnowman2.play();
    }

    private void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
        snowman1.getGroup().getTransforms().setAll(
                new Translate(snowman1X, snowman1Y, 0),
                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
        );

        // カメラの位置と回転を更新
        double snowmanCenterX = snowman1.getCenterX();
        double snowmanCenterY = snowman1.getCenterY();
        double snowmanCenterZ = snowman1.getCenterZ();

        camera.getTransforms().setAll(
                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
                new Translate(snowman1X, snowman1Y, -100)
        );
    }

    private void clearAttackEffect() {
        attackEffectGroup.getChildren().clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}