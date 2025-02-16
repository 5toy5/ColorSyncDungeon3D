//package toyoura.game;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.*;
//import javafx.scene.transform.Rotate;
//import javafx.scene.transform.Translate;
//import javafx.scene.PerspectiveCamera;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.io.File;
//import org.fxyz3d.importers.obj.ObjImporter;
//import org.fxyz3d.importers.Model3D;
//
//public class ViewApp extends Application {
//    static class Snowman {
//        Group group;
//        Sphere top;
//
//        public Snowman(double baseRadius, double topRadius, double offsetY) {
//            Sphere base = new Sphere(baseRadius);
//            top = new Sphere(topRadius);
//            top.getTransforms().add(new Translate(0, 0, -17));
//
//            Sphere eye1 = new Sphere(1);
//            eye1.setMaterial(new PhongMaterial(Color.BLACK));
//            eye1.getTransforms().add(new Translate(-2, -6, -20));
//
//            Sphere eye2 = new Sphere(1);
//            eye2.setMaterial(new PhongMaterial(Color.BLACK));
//            eye2.getTransforms().add(new Translate(2, -6, -20));
//
//            Cylinder nose = new Cylinder(0.5, 5);
//            nose.setMaterial(new PhongMaterial(Color.ORANGE));
//            nose.getTransforms().add(new Translate(0, -6, -17));
//
//            Cylinder leftArm = new Cylinder(0.5, 10);
//            leftArm.setMaterial(new PhongMaterial(Color.BLACK));
//            leftArm.getTransforms().add(new Translate(-10, 0, -10));
//            leftArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
//
//            Cylinder rightArm = new Cylinder(0.5, 10);
//            rightArm.setMaterial(new PhongMaterial(Color.BLACK));
//            rightArm.getTransforms().add(new Translate(10, 0, -10));
//            rightArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
//
//            group = new Group(base, top, eye1, eye2, nose, leftArm, rightArm);
//            group.getTransforms().add(new Translate(0, offsetY, 0));
//        }
//
//        public Group getGroup() {
//            return group;
//        }
//
//        public double getCenterX() {
//            return group.getBoundsInParent().getCenterX();
//        }
//
//        public double getCenterY() {
//            return group.getBoundsInParent().getCenterY();
//        }
//
//        public double getCenterZ() {
//            return group.getBoundsInParent().getCenterZ();
//        }
//
//        public Sphere getTop() {
//            return top;
//        }
//    }
//
//    private double snowman1X = 0;
//    private double snowman1Y = 0;
//    private double snowman1Z = 0;
//    private double snowman1Rotate = 0;
//    private double cameraarg = 0;
//    private Group root;
//    private boolean attack = false;
//    private Group attackEffectGroup = new Group();
//    private Group discGroup = new Group();
//    private Group importedObject; // インポートしたオブジェクトを保持
//    private Snowman snowman1;
//    private Snowman snowman2;
//    PerspectiveCamera camera;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        root = new Group();
//
//        snowman1 = new Snowman(10, 7, 0);
//        snowman2 = new Snowman(10, 7, 50);
//
//        Box Board = new Box(200, 200, 1);
//        Board.setTranslateZ(0);
//        Board.setMaterial(new PhongMaterial(Color.rgb(194, 178, 128)));
//
//        root.getChildren().addAll(Board, snowman1.getGroup(), snowman2.getGroup(), attackEffectGroup, discGroup);
//
//        camera = new PerspectiveCamera(true);
//        camera.setFieldOfView(45.0);
//        camera.setFarClip(200);
//
//        final Scene scene = new Scene(root, 800, 600, true);
//        scene.setCamera(camera);
//
//        stage.setScene(scene);
//        stage.setTitle("JavaFX 3D Snowmen");
//        stage.show();
//
//        // インポートオブジェクトのロード
//        importedObject = loadImportedObject();
//
////        // キーイベントの追加
////        scene.setOnKeyPressed(event -> {
////            switch (event.getCode()) {
////                case W:
////                    snowman1Y -= 3;
////                    break;
////                case S:
////                    snowman1Y += 3;
////                    break;
////                case A:
////                    snowman1X -= 3;
////                    break;
////                case D:
////                    snowman1X += 3;
////                    break;
////                case E:
////                    snowman1Rotate += 3;
////                    break;
////                case Q:
////                    snowman1Rotate -= 3;
////                    break;
////                case DIGIT1:
////                    attack = !attack;
////                    if (attack) {
////                        showAttackEffect(snowman1);
////                    } else {
////                        clearAttackEffect();
////                    }
////                    break;
////                case DIGIT2:
////                    if (discGroup.getChildren().isEmpty()) {
////                        showDiscAtSnowman2();
////                    } else {
////                        clearDisc();
////                    }
////                    break;
////                case J:
////                    cameraarg -= 3;
////                    break;
////                case K:
////                    cameraarg += 3;
////                    break;
////            }
////            updateSnowman1Position(snowman1, camera);
////        });
//
////        // 雪だるま2の回転アニメーション（必要に応じて）
////        Timeline timelineSnowman2 = new Timeline(
////                new KeyFrame(Duration.millis(10), event -> {
////                    snowman2.getGroup().getTransforms().setAll(new Translate(0, 0, 0));
////                    snowman2.getGroup().getTransforms().add(new Rotate(180, snowman2.getCenterX(), snowman2.getCenterY(), snowman2.getCenterZ(), Rotate.Z_AXIS));
////                })
////        );
////        timelineSnowman2.setCycleCount(Timeline.INDEFINITE);
////        timelineSnowman2.play();
////    }
//
////    private void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
////        snowman1.getGroup().getTransforms().setAll(
////                new Translate(snowman1X, snowman1Y, snowman1Z),
////                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
////        );
////
////        // カメラの位置と回転を更新
////        double snowmanCenterX = snowman1.getCenterX();
////        double snowmanCenterY = snowman1.getCenterY();
////        double snowmanCenterZ = snowman1.getCenterZ();
////
////        camera.getTransforms().setAll(
////                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
////                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
////                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
////                new Translate(snowman1X, snowman1Y, -100)
////        );
////    }
//
////    private void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
////        snowman1.getGroup().getTransforms().setAll(
////                new Translate(snowman1X, snowman1Y, snowman1Z),
////                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
////        );
////
////        // カメラの位置と回転を更新
////        double snowmanCenterX = snowman1.getCenterX();
////        double snowmanCenterY = snowman1.getCenterY();
////        double snowmanCenterZ = snowman1.getCenterZ();
////
////        camera.getTransforms().setAll(
////                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
////                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
////                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
////                new Translate(snowman1X, snowman1Y, -100)
////        );
////
////        // 攻撃エフェクトが有効であれば位置と回転を更新
////        if (attack) {
////            showAttackEffect(snowman1);
////        }
////    }
//
//    public void playerView(double snowman1X, double snowman1Y, double snowman1Rotate) {
//        this.snowman1X = snowman1X;
//        this.snowman1Y = snowman1Y;
//        this.snowman1Rotate = snowman1Rotate;
//        updateSnowman1Position(snowman1, camera);
//    }
//
//    private void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
//        snowman1.getGroup().getTransforms().setAll(
//                new Translate(snowman1X, snowman1Y, snowman1Z),
//                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
//        );
//
//        // カメラの位置と回転を更新
//        double snowmanCenterX = snowman1.getCenterX();
//        double snowmanCenterY = snowman1.getCenterY();
//        double snowmanCenterZ = snowman1.getCenterZ();
//
//        camera.getTransforms().setAll(
//                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
//                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
//                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
//                new Translate(snowman1X, snowman1Y, -100)
//        );
//
//        // 攻撃エフェクトが有効であれば位置と回転を更新
//        if (attack) {
//            showAttackEffect(snowman1);
//        }
//    }
//
//
////    private void showAttackEffect(Snowman snowman1) {
////        // 雪だるま1の前方にオブジェクトを配置
////        importedObject.getTransforms().setAll(
////                new Translate(snowman1X+20, snowman1Y+20, snowman1Z),
////                new Rotate(snowman1Rotate, Rotate.Z_AXIS)
////        );
////        attackEffectGroup.getChildren().add(importedObject);
////    }
//
//    private void showAttackEffect(Snowman snowman1) {
//        // 雪だるま1の位置と回転を取得
//        double snowmanPosX = snowman1X;
//        double snowmanPosY = snowman1Y;
//        double snowmanPosZ = snowman1Z;
//        double snowmanRotate = snowman1Rotate;
//
//        // 攻撃エフェクトを雪だるま1の位置に配置し、回転も合わせる
//        importedObject.getTransforms().setAll(
//                new Translate(snowmanPosX, snowmanPosY, snowmanPosZ),
//                new Rotate(snowmanRotate, Rotate.Z_AXIS),
//                new Translate(-10, 0, 0) // 雪だるまの前方に配置するためのオフセット
//        );
//
//        // 攻撃エフェクトをグループに追加（既に追加されていなければ）
//        if (!attackEffectGroup.getChildren().contains(importedObject)) {
//            attackEffectGroup.getChildren().add(importedObject);
//        }
//    }
//
////    private void clearAttackEffect() {
////        attackEffectGroup.getChildren().clear();
////    }
//
//    private void clearAttackEffect() {
//        attackEffectGroup.getChildren().remove(importedObject);
//    }
//
//    private Group loadImportedObject() throws Exception {
//        File file = new File("src/main/resources/3dmodels/testBody2.obj");
//        ObjImporter importer = new ObjImporter();
//        Model3D model = importer.load(file.toURI().toURL());
//        return model.getRoot();
//    }
//
//    public class DiscAtSnowman {
//
//        private Group discGroup;
//        private Color discColor;
//        private double discHeight;
//        private double centerZ;
//
//        public DiscAtSnowman(Group discGroup) {
//            this.discGroup = discGroup;
//            this.discColor = Color.BLUE; // デフォルトの色
//            this.discHeight = 1;         // デフォルトの厚み
//            this.centerZ = 7;            // デフォルトのZ座標
//        }
//
//        public void setDiscColor(Color color) {
//            this.discColor = color;
//        }
//
//        public void setDiscHeight(double height) {
//            this.discHeight = height;
//        }
//
//        public void setCenterZ(double z) {
//            this.centerZ = z;
//        }
//
//        public void showDiscAt(double x, double y, double radius) {
//            // 円盤（薄い円柱）を作成
//            double discRadius = radius;
//            Cylinder disc = new Cylinder(discRadius, this.discHeight);
//            disc.setMaterial(new PhongMaterial(this.discColor));
//
//            // 円盤をX軸回りに90度回転させて、ボードと平行にする
//            disc.getTransforms().addAll(
//                    new Rotate(90, Rotate.X_AXIS),
//                    new Translate(x, y, this.centerZ)
//            );
//
//            // 円盤をグループに追加
//            discGroup.getChildren().add(disc);
//        }
//
//        public void dokunumaView(double x, double y, double radius) {
//            showDiscAt(x, y, radius);
//        }
//    }
//
//    private void clearDisc() {
//        discGroup.getChildren().clear();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}






//package toyoura.game;
//
//import javafx.animation.KeyFrame;
//import javafx.animation.Timeline;
//import javafx.application.Application;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.*;
//import javafx.scene.transform.Rotate;
//import javafx.scene.transform.Translate;
//import javafx.scene.PerspectiveCamera;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//
//import java.io.File;
//import org.fxyz3d.importers.obj.ObjImporter;
//import org.fxyz3d.importers.Model3D;
//
//public class ViewApp extends Application {
//    static class Snowman {
//        Group group;
//        Sphere top;
//
//        public Snowman(double baseRadius, double topRadius, double offsetY) {
//            Sphere base = new Sphere(baseRadius);
//            top = new Sphere(topRadius);
//            top.getTransforms().add(new Translate(0, 0, -17));
//
//            Sphere eye1 = new Sphere(1);
//            eye1.setMaterial(new PhongMaterial(Color.BLACK));
//            eye1.getTransforms().add(new Translate(-2, -6, -20));
//
//            Sphere eye2 = new Sphere(1);
//            eye2.setMaterial(new PhongMaterial(Color.BLACK));
//            eye2.getTransforms().add(new Translate(2, -6, -20));
//
//            Cylinder nose = new Cylinder(0.5, 5);
//            nose.setMaterial(new PhongMaterial(Color.ORANGE));
//            nose.getTransforms().add(new Translate(0, -6, -17));
//
//            Cylinder leftArm = new Cylinder(0.5, 10);
//            leftArm.setMaterial(new PhongMaterial(Color.BLACK));
//            leftArm.getTransforms().add(new Translate(-10, 0, -10));
//            leftArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
//
//            Cylinder rightArm = new Cylinder(0.5, 10);
//            rightArm.setMaterial(new PhongMaterial(Color.BLACK));
//            rightArm.getTransforms().add(new Translate(10, 0, -10));
//            rightArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));
//
//            group = new Group(base, top, eye1, eye2, nose, leftArm, rightArm);
//            group.getTransforms().add(new Translate(0, offsetY, 0));
//        }
//
//        public Group getGroup() {
//            return group;
//        }
//
//        public double getCenterX() {
//            return group.getBoundsInParent().getCenterX();
//        }
//
//        public double getCenterY() {
//            return group.getBoundsInParent().getCenterY();
//        }
//
//        public double getCenterZ() {
//            return group.getBoundsInParent().getCenterZ();
//        }
//
//        public Sphere getTop() {
//            return top;
//        }
//    }
//
//    public double snowman1X = 0;
//    public double snowman1Y = 0;
//    public double snowman1Z = 0;
//    public double snowman1Rotate = 0;
//    public double cameraarg = 0;
//    public Group root;
//    public boolean attack = false;
//    public Group attackEffectGroup = new Group();
//    public Group discGroup = new Group();
//    public Group importedObject; // インポートしたオブジェクトを保持
//    public Snowman snowman1;
//    public Snowman snowman2;
//    public PerspectiveCamera camera;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        root = new Group();
//
//        snowman1 = new Snowman(10, 7, 100);
//        snowman2 = new Snowman(10, 7, 50);
//
//        Box Board = new Box(200, 200, 1);
//        Board.setTranslateZ(0);
//        Board.setMaterial(new PhongMaterial(Color.rgb(194, 178, 128)));
//
//        root.getChildren().addAll(Board, snowman1.getGroup(), snowman2.getGroup(), attackEffectGroup, discGroup);
//
//        camera = new PerspectiveCamera(true);
//        camera.setFieldOfView(45.0);
//        camera.setFarClip(200);
//
//        final Scene scene = new Scene(root, 800, 600, true);
//        scene.setCamera(camera);
//
//        stage.setScene(scene);
//        stage.setTitle("JavaFX 3D Snowmen");
//        stage.show();
//
//        // インポートオブジェクトのロード
//        importedObject = loadImportedObject();
//
//        // キーイベントの追加
//        scene.setOnKeyPressed(event -> {
//            switch (event.getCode()) {
//                case W:
//                    snowman1Y -= 3;
//                    break;
//                case S:
//                    snowman1Y += 3;
//                    break;
//                case A:
//                    snowman1X -= 3;
//                    break;
//                case D:
//                    snowman1X += 3;
//                    break;
//                case E:
//                    snowman1Rotate += 3;
//                    break;
//                case Q:
//                    snowman1Rotate -= 3;
//                    break;
//                case DIGIT1:
//                    attack = !attack;
//                    if (attack) {
//                        showAttackEffect(snowman1);
//                    } else {
//                        clearAttackEffect();
//                    }
//                    break;
//                case DIGIT2:
//                    if (discGroup.getChildren().isEmpty()) {
//                        showDiscAtSnowman2();
//                    } else {
//                        clearDisc();
//                    }
//                    break;
//                case J:
//                    cameraarg -= 3;
//                    break;
//                case K:
//                    cameraarg += 3;
//                    break;
//            }
//            updateSnowman1Position(snowman1, camera);
//        });
//
//        // 雪だるま2の回転アニメーション（必要に応じて）
//        Timeline timelineSnowman2 = new Timeline(
//                new KeyFrame(Duration.millis(10), event -> {
//                    snowman2.getGroup().getTransforms().setAll(new Translate(0, 0, 0));
//                    snowman2.getGroup().getTransforms().add(new Rotate(180, snowman2.getCenterX(), snowman2.getCenterY(), snowman2.getCenterZ(), Rotate.Z_AXIS));
//                })
//        );
//        timelineSnowman2.setCycleCount(Timeline.INDEFINITE);
//        timelineSnowman2.play();
//    }
//
////    public void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
////        snowman1.getGroup().getTransforms().setAll(
////                new Translate(snowman1X, snowman1Y, snowman1Z),
////                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
////        );
////
////        // カメラの位置と回転を更新
////        double snowmanCenterX = snowman1.getCenterX();
////        double snowmanCenterY = snowman1.getCenterY();
////        double snowmanCenterZ = snowman1.getCenterZ();
////
////        camera.getTransforms().setAll(
////                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
////                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
////                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
////                new Translate(snowman1X, snowman1Y, -100)
////        );
////    }
//
////    public void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
////        snowman1.getGroup().getTransforms().setAll(
////                new Translate(snowman1X, snowman1Y, snowman1Z),
////                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
////        );
////
////        // カメラの位置と回転を更新
////        double snowmanCenterX = snowman1.getCenterX();
////        double snowmanCenterY = snowman1.getCenterY();
////        double snowmanCenterZ = snowman1.getCenterZ();
////
////        camera.getTransforms().setAll(
////                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
////                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
////                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
////                new Translate(snowman1X, snowman1Y, -100)
////        );
////
////        // 攻撃エフェクトが有効であれば位置と回転を更新
////        if (attack) {
////            showAttackEffect(snowman1);
////        }
////    }
//
//    public void updateSnowman1PosisionForEngine(double snowman1X, double snowman1Y, double snowman1Rotate) {
//        this.snowman1X = snowman1X;
//        this.snowman1Y = snowman1Y;
//        this.snowman1Rotate = snowman1Rotate;
//        updateSnowman1Position(snowman1,camera);
//    }
//
//    public void updateSnowman1Position(Snowman snowman1, PerspectiveCamera camera) {
//        snowman1.getGroup().getTransforms().setAll(
//                new Translate(snowman1X, snowman1Y, snowman1Z),
//                new Rotate(snowman1Rotate, 0, 0, 0, Rotate.Z_AXIS)
//        );
//
//        // カメラの位置と回転を更新
//        double snowmanCenterX = snowman1.getCenterX();
//        double snowmanCenterY = snowman1.getCenterY();
//        double snowmanCenterZ = snowman1.getCenterZ();
//
//        camera.getTransforms().setAll(
//                new Rotate(snowman1Rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
//                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
//                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
//                new Translate(snowman1X, snowman1Y, -100)
//        );
//
//        // 攻撃エフェクトが有効であれば位置と回転を更新
//        if (attack) {
//            showAttackEffect(snowman1);
//        }
//    }
//
////    public void showAttackEffect(Snowman snowman1) {
////        // 雪だるま1の前方にオブジェクトを配置
////        importedObject.getTransforms().setAll(
////                new Translate(snowman1X+20, snowman1Y+20, snowman1Z),
////                new Rotate(snowman1Rotate, Rotate.Z_AXIS)
////        );
////        attackEffectGroup.getChildren().add(importedObject);
////    }
//
//    public void showAttackEffect(Snowman snowman1) {
//        // 雪だるま1の位置と回転を取得
//        double snowmanPosX = snowman1X;
//        double snowmanPosY = snowman1Y;
//        double snowmanPosZ = snowman1Z;
//        double snowmanRotate = snowman1Rotate;
//
//        // 攻撃エフェクトを雪だるま1の位置に配置し、回転も合わせる
//        importedObject.getTransforms().setAll(
//                new Translate(snowmanPosX, snowmanPosY, snowmanPosZ),
//                new Rotate(snowmanRotate, Rotate.Z_AXIS),
//                new Translate(-10, 0, 0) // 雪だるまの前方に配置するためのオフセット
//        );
//
//        // 攻撃エフェクトをグループに追加（既に追加されていなければ）
//        if (!attackEffectGroup.getChildren().contains(importedObject)) {
//            attackEffectGroup.getChildren().add(importedObject);
//        }
//    }
//
////    public void clearAttackEffect() {
////        attackEffectGroup.getChildren().clear();
////    }
//
//    public void clearAttackEffect() {
//        attackEffectGroup.getChildren().remove(importedObject);
//    }
//
//    public Group loadImportedObject() throws Exception {
//        File file = new File("src/main/resources/3dmodels/testBody2.obj");
//        ObjImporter importer = new ObjImporter();
//        Model3D model = importer.load(file.toURI().toURL());
//        return model.getRoot();
//    }
//
//    public void showDiscAtSnowman2() {
//        // 円盤（薄い円柱）を作成
//        double discRadius = 20; // 半径を調整
//        double discHeight = 1;  // 厚みを調整
//        Cylinder disc = new Cylinder(discRadius, discHeight);
//        disc.setMaterial(new PhongMaterial(Color.BLUE));
//
//        // 雪だるま2の中心位置を取得
//        double centerX = snowman2.getCenterX();
//        double centerY = snowman2.getCenterY();
//        double centerZ = snowman2.getCenterZ();
//
//        // 円盤をX軸回りに90度回転させて、ボードと平行にする
//        disc.getTransforms().addAll(
//                new Rotate(90, Rotate.X_AXIS), // 回転を先に適用
//                new Translate(centerX, centerY - 3, centerZ) // 位置を設定
//        );
//
//        // 円盤をグループに追加
//        discGroup.getChildren().add(disc);
//    }
//
//    public void clearDisc() {
//        discGroup.getChildren().clear();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}