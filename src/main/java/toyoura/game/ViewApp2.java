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
//import java.util.Objects;
//
//import org.fxyz3d.importers.obj.ObjImporter;
//import org.fxyz3d.importers.Model3D;
//
//public class ViewApp2 extends Application {
//
//    public ViewApp2() throws Exception {
//    }
//
//    static class Snowman {
//        double x,y,z,rotate;
//        double baseRadius,topRadius,offsetY;
//        String str;
//        Group group;
//
//        Snowman(double x,double y,double z,double rotate,double baseRadius,double topRadius,double offsetY,String str){
//            this.x = x;
//            this.y = y;
//            this.z = z;
//            this.rotate = rotate;
//            this.baseRadius = baseRadius;
//            this.topRadius = topRadius;
//            this.offsetY = offsetY;
//            this.str = str;
//            drawSnowman();
//        }
//
//        void drawSnowman() {
//            Sphere base = new Sphere(baseRadius);
//            Sphere top = new Sphere(topRadius);
//            if(Objects.equals(str, "red")) {base.setMaterial(new PhongMaterial(Color.RED)); top.setMaterial(new PhongMaterial(Color.RED));}
//            else if(Objects.equals(str, "green")) {base.setMaterial(new PhongMaterial(Color.GREEN)); top.setMaterial(new PhongMaterial(Color.GREEN));}
//            else if(Objects.equals(str, "blue")) {base.setMaterial(new PhongMaterial(Color.BLUE)); top.setMaterial(new PhongMaterial(Color.BLUE));}
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
//        void update(){
//            this.group.getTransforms().setAll(
//                    new Translate(x, y,z),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//        }
//    }
//
//    static class SphereObject {
//        double x, y, radius;
//        String str;
//        Group group;
//
//        SphereObject(double x, double y, double radius, String str) {
//            this.x = x;
//            this.y = y;
//            this.radius = radius;
//            this.str = str;
//
//            drawSphere();
//        }
//
//        void drawSphere() {
//            Sphere sphere = new Sphere(radius);
//            if(Objects.equals(str, "red")) sphere.setMaterial(new PhongMaterial(Color.RED));
//            else if(Objects.equals(str, "green")) sphere.setMaterial(new PhongMaterial(Color.GREEN));
//            else if(Objects.equals(str, "blue")) sphere.setMaterial(new PhongMaterial(Color.BLUE));
//            sphere.getTransforms().add(new Translate(x, y,0));
//
//            group = new Group(sphere);
//        }
//
//        public Group getGroup() {
//            return group;
//        }
//    }
//
//    static class ImportedObject{
//        double x, y, z, rotate;
//        String str;
//        Group group;
//        File file;
//
//        ImportedObject(double x, double y, double rotate, String str) throws Exception{
//            this.x = x;
//            this.y = y;
//            this.z = -20;
//            this.rotate = rotate;
//            this.str = str;
//            loadImportedObject();
//            update();
//        }
//
//        void loadImportedObject() throws Exception {
//            if(Objects.equals(str, "red")) file = new File("src/main/resources/3dmodels/medipro_devil.obj");
//            if(Objects.equals(str, "green")) file = new File("src/main/resources/3dmodels/testBody2.obj");
//            else if(Objects.equals(str, "blue")) file = new File("src/main/resources/3dmodels/medipro_devil.obj");
//            ObjImporter importer = new ObjImporter();
//            Model3D model = importer.load(file.toURI().toURL());
//            group = model.getRoot();
//        }
//
//        public Group getGroup() {
//            return group;
//        }
//
//        void update() {
//            this.group.getTransforms().setAll(
//                    new Translate(x, y, z),
//                    new Rotate(180, 0,0,0,Rotate.X_AXIS),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//        }
//    }
//
//    static class Disc {
//        double x,y,z,radius;
//        String str;
//        Group group;
//
//        public Disc(double x, double y, double radius) {
//            this.x = x;
//            this.y = y;
//            this.z = -2;
//            this.radius = radius;
//            showDisc();
//        }
//
//        public void showDisc() {
//            double discRadius = radius;
//            double discHeight = 1;
//            Cylinder disc = new Cylinder(discRadius, discHeight);
//            if(Objects.equals(str, "red")) disc.setMaterial(new PhongMaterial(Color.RED));
//            else if(Objects.equals(str, "green")) disc.setMaterial(new PhongMaterial(Color.GREEN));
//            else if(Objects.equals(str, "blue")) disc.setMaterial(new PhongMaterial(Color.BLUE));
//            double centerX = x;
//            double centerY = y;
//            double centerZ = z;
//            disc.getTransforms().addAll(
//                    new Rotate(90, Rotate.X_AXIS),
//                    new Translate(centerX, centerY - 3, centerZ)
//            );
//            group = new Group(disc);
//        }
//        public void clearDisc() {
//            group.getChildren().clear();
//        }
//
//        public Group getGroup() {
//            return group;
//        }
//    }
//
//
//    Snowman player = new Snowman(0,0,0,0,10,7,0,"blue");
//    Snowman s2 = new Snowman(0,0,0,0,10,7,50,"blue");
////    SphereObject sphere = new SphereObject(10, 10, 2, "red");
////    ImportedObject monster = new ImportedObject(10,10,0,"blue");
////    Disc disc = new Disc(-10,-10,30);
//
//    private double cameraarg = 0;
//    private static Group root;
//    private boolean attack = false;
//    private Group attackEffectGroup = new Group();
//    private Group group = new Group();
//    private Group importedObject; // インポートしたオブジェクトを保持
//    public PerspectiveCamera camera;
//
//    @Override
//    public void start(Stage stage) throws Exception {
//        root = new Group();
//        root.getChildren().add(player.getGroup());
//        root.getChildren().add(s2.getGroup());
////        root.getChildren().add(sphere.getGroup());
////        root.getChildren().add(monster.getGroup());
////        root.getChildren().add(disc.getGroup());
//
////        // ランダムなSphereObjectのインスタンスを生成してrootに追加
////        for (int i = 0; i < 5; i++) {
////            SphereObject sphereObject = new SphereObject(Math.random() * 30, Math.random() * 30, 10 + Math.random() * 5);
////            root.getChildren().add(sphereObject.getGroup());
////        }
//
//        //地面の描画
//        Box Board = new Box(200, 200, 1);
//        Board.setTranslateZ(10);
//        Board.setMaterial(new PhongMaterial(Color.rgb(194, 178, 128)));
//
//        root.getChildren().addAll(Board, attackEffectGroup, group); //player.group, s2.group, , sphere1.getGroup()
//
//        // カメラの初期化
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
////        // インポートオブジェクトのロード
////        importedObject = loadImportedObject();
////        if (importedObject == null) {
////            System.out.println("Failed to load the imported object.");
////            return;
////        }
//
//        // キーイベントの追加
//        scene.setOnKeyPressed(event -> {
//            switch (event.getCode()) {
//                case W:
//                    player.y -= 3;
//                    break;
//                case S:
//                    player.y += 3;
//                    break;
//                case A:
//                    player.x -= 3;
//                    break;
//                case D:
//                    player.x += 3;
//                    break;
//                case E:
//                    player.rotate += 3;
//                    break;
//                case Q:
//                    player.rotate -= 3;
//                    break;
////                case DIGIT1:
////                    attack = !attack;
////                    if (attack) {
////                        showAttackEffect(player);
////                    } else {
////                        clearAttackEffect();
////                    }
////                    break;
////                case DIGIT2:
////                    if (group.getChildren().isEmpty()) {
////                        showDiscAtSnowman2();
////                    } else {
////                        clearDisc();
////                    }
////                    break;
//                case J:
//                    cameraarg -= 3;
//                    break;
//                case K:
//                    cameraarg += 3;
//                    break;
//            }
//            player.update();
//
//            // カメラの位置と回転を更新
//            double snowmanCenterX = player.getCenterX();
//            double snowmanCenterY = player.getCenterY();
//            double snowmanCenterZ = player.getCenterZ();
//
//            camera.getTransforms().setAll(
//                    new Rotate(player.rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
//                    new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
//                    new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
//                    new Translate(player.x, player.y, -100)
//            );
//
////            // 攻撃エフェクトが有効であれば位置と回転を更新
////            if (attack) {
////                showAttackEffect(player);
////            }
//        });
//
//        // 雪だるま2の回転アニメーション（必要に応じて）
//        Timeline timelineSnowman2 = new Timeline(
//                new KeyFrame(Duration.millis(10), event -> {
//                    s2.getGroup().getTransforms().setAll(new Translate(0, 0, 0));
//                    s2.getGroup().getTransforms().add(new Rotate(180, s2.getCenterX(), s2.getCenterY(), s2.getCenterZ(), Rotate.Z_AXIS));
//                })
//        );
//        timelineSnowman2.setCycleCount(Timeline.INDEFINITE);
//        timelineSnowman2.play();
//    }
//
////    public static void PlayerView(double x, double y, double rotate){
////        Snowman player = new Snowman(x,y,0,rotate,10,7,0);
////        root.getChildren().add(player.getGroup());
////    }
//
//    private void updateplayerPosition() {
//        player.update();
//
//        // カメラの位置と回転を更新
//        double snowmanCenterX = player.getCenterX();
//        double snowmanCenterY = player.getCenterY();
//        double snowmanCenterZ = player.getCenterZ();
//
//        camera.getTransforms().setAll(
//                new Rotate(player.rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
//                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
//                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
//                new Translate(player.x, player.y, -100)
//        );
//
////        // 攻撃エフェクトが有効であれば位置と回転を更新
////        if (attack) {
////            showAttackEffect(player);
////        }
//    }
//
//
////    private void showAttackEffect(Snowman s) {
////        // 攻撃エフェクトを雪だるま1の位置に配置し、回転も合わせる
////        importedObject.getTransforms().setAll(
////                new Translate(s.x,s.y,s.z),
////                new Rotate(s.rotate, Rotate.Z_AXIS),
////                new Translate(-10, 0, 0) // 雪だるまの前方に配置するためのオフセット
////        );
////
////        // 攻撃エフェクトをグループに追加（既に追加されていなければ）
////        if (!attackEffectGroup.getChildren().contains(importedObject)) {
////            attackEffectGroup.getChildren().add(importedObject);
////        }
////    }
//
////    private void clearAttackEffect() {
////        attackEffectGroup.getChildren().clear();
////    }
//
//    private void clearAttackEffect() {
//        attackEffectGroup.getChildren().remove(importedObject);
//    }
//
////    private Group loadImportedObject() throws Exception {
////        File file = new File("src/main/resources/3dmodels/testBody2.obj");
////        ObjImporter importer = new ObjImporter();
////        Model3D model = importer.load(file.toURI().toURL());
////        return model.getRoot();
////    }
//
////    private void showDiscAtSnowman2() {
////        // 円盤（薄い円柱）を作成
////        double discRadius = 20; // 半径を調整
////        double discHeight = 1;  // 厚みを調整
////        Cylinder disc = new Cylinder(discRadius, discHeight);
////        disc.setMaterial(new PhongMaterial(Color.BLUE));
////
////        // 雪だるま2の中心位置を取得
////        double centerX = s2.getCenterX();
////        double centerY = s2.getCenterY();
////        double centerZ = s2.getCenterZ();
////
////        // 円盤をX軸回りに90度回転させて、ボードと平行にする
////        disc.getTransforms().addAll(
////                new Rotate(90, Rotate.X_AXIS), // 回転を先に適用
////                new Translate(centerX, centerY - 3, centerZ) // 位置を設定
////        );
////
////        // 円盤をグループに追加
////        group.getChildren().add(disc);
////    }
//
////    private void clearDisc() {
////        group.getChildren().clear();
////    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}