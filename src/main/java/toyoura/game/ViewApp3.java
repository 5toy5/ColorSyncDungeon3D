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
//
//import javafx.scene.transform.Rotate;
//import javafx.scene.transform.Translate;
//import javafx.scene.PerspectiveCamera;
//import javafx.stage.Stage;
//import javafx.util.Duration;
//import java.io.File;
//import java.util.Objects;
//
//import org.fxyz3d.importers.obj.ObjImporter;
//import org.fxyz3d.importers.Model3D;
//
//import javafx.animation.AnimationTimer;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.input.KeyCode;
//import javafx.scene.layout.StackPane;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
//
//
//public class ViewApp3 extends Application {
//
//    public ViewApp3() throws Exception {
//    }
//
//    static class Snowman {
//        double x, y, z, rotate;
//        double baseRadius, topRadius, offsetY;
//        String str;
//        Group group;
//
//        Snowman(double x, double y, double z, double rotate, double baseRadius, double topRadius, double offsetY, String str) {
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
//            if (Objects.equals(str, "red")) {
//                base.setMaterial(new PhongMaterial(Color.RED));
//                top.setMaterial(new PhongMaterial(Color.RED));
//            } else if (Objects.equals(str, "green")) {
//                base.setMaterial(new PhongMaterial(Color.GREEN));
//                top.setMaterial(new PhongMaterial(Color.GREEN));
//            } else if (Objects.equals(str, "blue")) {
//                base.setMaterial(new PhongMaterial(Color.BLUE));
//                top.setMaterial(new PhongMaterial(Color.BLUE));
//            }
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
//        void setX(double newX) {
//            this.x = newX;
//        }
//
//        void setY(double newY) {
//            this.y = newY;
//        }
//
//        void setRotate(double newR) {
//            this.rotate = newR;
//        }
//
//        void update() {
//            this.group.getTransforms().setAll(
//                    new Translate(x, y, z),
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
//            if (Objects.equals(str, "red")) sphere.setMaterial(new PhongMaterial(Color.RED));
//            else if (Objects.equals(str, "green")) sphere.setMaterial(new PhongMaterial(Color.GREEN));
//            else if (Objects.equals(str, "blue")) sphere.setMaterial(new PhongMaterial(Color.BLUE));
//            sphere.getTransforms().add(new Translate(x, y, 0));
//
//            group = new Group(sphere);
//        }
//
//        public Group getGroup() {
//            return group;
//        }
//    }
//
//    static class ImportedObject {
//        double x, y, z, rotate;
//        String str;
//        Group group;
//        File file;
//
//        ImportedObject(double x, double y, double rotate, String str) throws Exception {
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
//            if (Objects.equals(str, "red")) file = new File("src/main/resources/3dmodels/medipro_devil.obj");
//            if (Objects.equals(str, "green")) file = new File("src/main/resources/3dmodels/medipro_devil.obj");
//            else if (Objects.equals(str, "blue")) file = new File("src/main/resources/3dmodels/medipro_devil.obj");
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
//                    new Rotate(180, 0, 0, 0, Rotate.X_AXIS),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//        }
//    }
//
//    static class Disc {
//        double x, y, z, radius;
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
//            if (Objects.equals(str, "red")) disc.setMaterial(new PhongMaterial(Color.RED));
//            else if (Objects.equals(str, "green")) disc.setMaterial(new PhongMaterial(Color.GREEN));
//            else if (Objects.equals(str, "blue")) disc.setMaterial(new PhongMaterial(Color.BLUE));
//            double centerX = x;
//            double centerY = y;
//            double centerZ = z;
//            disc.getTransforms().addAll(
//                    new Rotate(90, Rotate.X_AXIS),
//                    new Translate(centerX, centerY - 3, centerZ)
//            );
//            group = new Group(disc);
//        }
//
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
//    Snowman player = new Snowman(0, 0, 0, 0, 10, 7, 0, "blue");
//    Snowman s2 = new Snowman(0, 0, 0, 0, 10, 7, 50, "blue");
//    SphereObject sphere = new SphereObject(10, 10, 2, "red");
//    ImportedObject monster = new ImportedObject(10, 10, 0, "blue");
//    Disc disc = new Disc(-10, -10, 30);
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
//        root.getChildren().add(sphere.getGroup());
//        root.getChildren().add(monster.getGroup());
//        root.getChildren().add(disc.getGroup());
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
////        // キーイベントの追加
////        scene.setOnKeyPressed(event -> {
////            switch (event.getCode()) {
////                case W:
////                    player.y -= 3;
////                    break;
////                case S:
////                    player.y += 3;
////                    break;
////                case A:
////                    player.x -= 3;
////                    break;
////                case D:
////                    player.x += 3;
////                    break;
////                case E:
////                    player.rotate += 3;
////                    break;
////                case Q:
////                    player.rotate -= 3;
////                    break;
//////                case DIGIT1:
//////                    attack = !attack;
//////                    if (attack) {
//////                        showAttackEffect(player);
//////                    } else {
//////                        clearAttackEffect();
//////                    }
//////                    break;
//////                case DIGIT2:
//////                    if (group.getChildren().isEmpty()) {
//////                        showDiscAtSnowman2();
//////                    } else {
//////                        clearDisc();
//////                    }
//////                    break;
////                case J:
////                    cameraarg -= 3;
////                    break;
////                case K:
////                    cameraarg += 3;
////                    break;
////            }
////            player.update();
////
////            // カメラの位置と回転を更新
////            double snowmanCenterX = player.getCenterX();
////            double snowmanCenterY = player.getCenterY();
////            double snowmanCenterZ = player.getCenterZ();
////
////            camera.getTransforms().setAll(
////                    new Rotate(player.rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
////                    new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
////                    new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
////                    new Translate(player.x, player.y, -100)
////            );
////
//////            // 攻撃エフェクトが有効であれば位置と回転を更新
//////            if (attack) {
//////                showAttackEffect(player);
//////            }
////        });
//
////        // 雪だるま2の回転アニメーション（必要に応じて）
////        Timeline timelineSnowman2 = new Timeline(
////                new KeyFrame(Duration.millis(10), event -> {
////                    s2.getGroup().getTransforms().setAll(new Translate(0, 0, 0));
////                    s2.getGroup().getTransforms().add(new Rotate(180, s2.getCenterX(), s2.getCenterY(), s2.getCenterZ(), Rotate.Z_AXIS));
////                })
////        );
////        timelineSnowman2.setCycleCount(Timeline.INDEFINITE);
////        timelineSnowman2.play();
//    }
//
//
////    public static void PlayerView(double x, double y, double rotate){
////        Snowman player = new Snowman(x,y,0,rotate,10,7,0);
////        root.getChildren().add(player.getGroup());
////    }
//
//    public void updateplayerPosition() {
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
//
//
//        //ゲームのグローバル変数
//        toyoura.game.Engine.Player playerEngine;
//        ArrayList<toyoura.game.Engine.Enemy> enemies;
//        toyoura.game.Engine.Attack attackEngine;
//        ArrayList<toyoura.game.Engine.Bullet> bullets;
//        ArrayList<toyoura.game.Engine.Orbit> orbits;
//        ArrayList<toyoura.game.Engine.Wave> waves;
//        boolean gameOver;
//        HashMap<KeyCode, Boolean> keys = new HashMap<>();
//        int bgColor;
//
//        // キャンバス
//        final int width = 800;
//        final int height = 600;
//
//        long lastNanoTime;
//
//        @Override
//        public void start(Stage primaryStage) {
//            playerEngine = new toyoura.game.Engine.Player(width / 2, height / 2);
//            attackEngine = new toyoura.game.Engine.Attack(playerEngine.x, playerEngine.y, playerEngine.angle, playerEngine.type);
//            enemies = new ArrayList<>();
//            bullets = new ArrayList<>();
//            orbits = new ArrayList<>();
//            waves = new ArrayList<>();
//            gameOver = false;
//
//            for (int i = 0; i < 5; i++) {
//                enemies.add(new toyoura.game.Engine.Enemy());
//            }
//
//            Canvas canvas = new Canvas(width, height);
//            GraphicsContext gc = canvas.getGraphicsContext2D();
//
//            StackPane root = new StackPane();
//            root.getChildren().addAll(canvas);
//
//            Scene scene = new Scene(root);
//            primaryStage.setScene(scene);
//            primaryStage.setTitle("JavaFX Game Recreation");
//            primaryStage.show();
//
//            // 押されているキーの管理
//            scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
//            scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
//
//            lastNanoTime = System.nanoTime();
//
//            new AnimationTimer() {
//                @Override
//                public void handle(long currentNanoTime) {
//                    double elapsedTime = (currentNanoTime - lastNanoTime) / 1_000_000.0;
//                    lastNanoTime = currentNanoTime;
//
//                    handlePlayerInput();
//                    updateGame(elapsedTime);
//
//                    render(gc);
//                }
//            }.start();
//        }
//
//        void handlePlayerInput() {
//            if (isKeyPressed(KeyCode.W)) playerEngine.move(0);
//            if (isKeyPressed(KeyCode.S)) playerEngine.move(Math.PI);
//            if (isKeyPressed(KeyCode.A)) playerEngine.move(-Math.PI / 2);
//            if (isKeyPressed(KeyCode.D)) playerEngine.move(Math.PI / 2);
//
//            if (isKeyPressed(KeyCode.Q)) playerEngine.rotate(-0.1);
//            if (isKeyPressed(KeyCode.E)) playerEngine.rotate(0.1);
//
//            if (isKeyPressed(KeyCode.R)) playerEngine.changeType("red");
//            if (isKeyPressed(KeyCode.G)) playerEngine.changeType("green");
//            if (isKeyPressed(KeyCode.B)) playerEngine.changeType("blue");
//
//            if (isKeyPressed(KeyCode.SPACE)) playerEngine.attackEngine(attackEngine);
//        }
//
//        void updateGame(double elapsedTime) {
//            if (gameOver) return;
//
//            //敵の更新
//            for (toyoura.game.Engine.Enemy enemy : enemies) {
//                enemy.move();
//                enemy.attackEngine();
//            }
//
//            // 攻撃の更新
//            if (attackEngine.isActive()) {
//                for (toyoura.game.Engine.Enemy enemy : enemies) {
//                    if (attackEngine.isHit(enemy)) {
//                        enemy.respawn();
//                    }
//                }
//            }
//
//            // 銃弾の更新
//            Iterator<toyoura.game.Engine.Bullet> bulletIter = bullets.iterator();
//            while (bulletIter.hasNext()) {
//                toyoura.game.Engine.Bullet bullet = bulletIter.next();
//                bullet.move();
//                if (bullet.isOutOfBounds(width, height)) {
//                    bulletIter.remove();
//                } else if (bullet.isHit(playerEngine)) {
//                    bulletIter.remove();
//                    playerEngine.damaged();
//                }
//            }
//
//            // 軌道弾の更新
//            Iterator<toyoura.game.Engine.Orbit> orbitIter = orbits.iterator();
//            while (orbitIter.hasNext()) {
//                toyoura.game.Engine.Orbit orbit = orbitIter.next();
//                if (orbit.isActive()) {
//                    orbit.move();
//                    if (orbit.isOutOfBounds(width, height)) {
//                        orbitIter.remove();
//                    } else if (orbit.isHit(playerEngine)) {
//                        orbitIter.remove();
//                        playerEngine.damaged();
//                    }
//                } else {
//                    orbitIter.remove();
//                }
//            }
//
//            // 波動の更新
//            Iterator<toyoura.game.Engine.Wave> waveIter = waves.iterator();
//            while (waveIter.hasNext()) {
//                toyoura.game.Engine.Wave wave = waveIter.next();
//                if (wave.isActive()) {
//                    wave.update();
//                    if (wave.isHit(playerEngine)) {
//                        playerEngine.damaged();
//                    }
//                } else {
//                    waveIter.remove();
//                }
//            }
//
//            // 敵との衝突判定
//            for (toyoura.game.Engine.Enemy enemy : enemies) {
//                if (enemy.isCollidingWith(playerEngine)) {
//                    enemy.respawn();
//                    playerEngine.damaged();
//                }
//            }
//        }
//
//        void render(GraphicsContext gc) {
//            //キャンバスのリセット
//            gc.setFill(Color.rgb(bgColor, bgColor, bgColor));
//            gc.fillRect(0, 0, width, height);
//
//            //ゲームオーバー
//            if (gameOver) {
//                gc.setFill(Color.WHITE);
//                gc.fillText("Game Over", width / 2 - 50, height / 2);
//                return;
//            }
//
//            // プレイヤーの描画
//            playerEngine.display(gc);
//
//            // 敵の描画
//            for (toyoura.game.Engine.Enemy enemy : enemies) {
//                enemy.display(gc);
//            }
//
//            // 攻撃の描画
//            if (attackEngine.isActive()) {
//                attackEngine.display(gc);
//            }
//
//            // 銃弾の描画
//            for (toyoura.game.Engine.Bullet bullet : bullets) {
//                bullet.display(gc);
//            }
//
//            // 軌道弾の描画
//            for (toyoura.game.Engine.Orbit orbit : orbits) {
//                orbit.display(gc);
//            }
//
//            // 波動の描画
//            for (toyoura.game.Engine.Wave wave : waves) {
//                wave.display(gc);
//            }
//        }
//
//        boolean isKeyPressed(KeyCode key) {
//            return keys.getOrDefault(key, false);
//        }
//
//        Color getColor(String type) {
//            switch (type) {
//                case "red":
//                    return Color.RED;
//                case "green":
//                    return Color.LIME;
//                case "blue":
//                    return Color.BLUE;
//                default:
//                    return Color.BLACK;
//            }
//        }
//
//        boolean isEffective(String attackEngineType, String targetType) {
//            return (attackEngineType.equals("red") && targetType.equals("green")) ||
//                    (attackEngineType.equals("green") && targetType.equals("blue")) ||
//                    (attackEngineType.equals("blue") && targetType.equals("red"));
//        }
//
//        public static void main(String[] args) {
//            launch(args);
//        }
//
//        // Playerクラス
//        class Player {
//            double x, y, angle, speed;
//            String type;
//            int hp;
//            long lastDamagedTime;
//
//            Player(double x, double y) {
//                this.x = x;
//                this.y = y;
//                this.angle = 1;
//                this.speed = 3;
//                this.hp = 10;
//                this.type = "red";
//                this.lastDamagedTime = System.currentTimeMillis();
//            }
//
//            void move(double da) {
//                x += Math.cos(angle + da) * speed;
//                y += Math.sin(angle + da) * speed;
//                x = clamp(x, 0, width);
//                y = clamp(y, 0, height);
//            }
//
//            void rotate(double da) {
//                angle += da;
//            }
//
//            void changeType(String newType) {
//                type = newType;
//            }
//
//            void attackEngine(toyoura.game.Engine.Attack a) {
//                a.newAttack(x, y, angle, type);
//            }
//
//            void damaged() {
//                if (System.currentTimeMillis() - lastDamagedTime > 200) {
//                    hp -= 1;
//                    if (hp <= 0) gameOver = true;
//                    lastDamagedTime = System.currentTimeMillis();
//                }
//            }
//
//            //x,y,angle,type
//            void display(GraphicsContext gc) {
////            bgColor = 25 * hp;
////
////            gc.setFill(getColor(type));
////            gc.fillOval(x - 15, y - 15, 30, 30);
////
////            gc.setFill(Color.WHITE);
////            gc.fillOval(
////                    x + Math.cos(angle) * 10 - 5,
////                    y + Math.sin(angle) * 10 - 5,
////                    10, 10
////            );
////
//            }
//        }
//
//        // Enemyクラス
//        class Enemy {
//            double x, y, angle, speed, da;
//            String type;
//            long lastBulletTime, nextBulletTime;
//            long lastOrbitTime, nextOrbitTime;
//            long lastWaveTime, nextWaveTime;
//
//            Enemy() {
//                respawn();
//                long currentTime = System.currentTimeMillis();
//                lastBulletTime = currentTime;
//                lastOrbitTime = currentTime;
//                lastWaveTime = currentTime;
//            }
//
//            void respawn() {
//                x = Math.random() * width;
//                y = Math.random() * height;
//                angle = Math.random() * (2 * Math.PI) - Math.PI;
//                speed = 1;
//                type = randomType();
//            }
//
//            void move() {
//                angle += da;
//                x += Math.cos(angle) * speed;
//                y += Math.sin(angle) * speed;
//                x = wrap(x, 0, width);
//                y = wrap(y, 0, height);
//            }
//
//            void attackEngine() {
//                long currentTime = System.currentTimeMillis();
//
//                // 銃弾攻撃
//                if (currentTime - lastBulletTime > nextBulletTime) {
//                    angle = Math.atan2(playerEngine.y - y, playerEngine.x - x);
//                    da = Math.random() * (Math.PI / 25) - (Math.PI / 50);
//                    bullets.add(new toyoura.game.ViewApp3.Bullet(x, y, angle, type));
//                    lastBulletTime = currentTime;
//                    nextBulletTime = (long) (Math.random() * 2000) + 1000;
//                }
//
//                // 軌道弾攻撃
//                if (currentTime - lastOrbitTime > nextOrbitTime) {
//                    for (int i = 0; i < 10; i++) {
//                        orbits.add(new toyoura.game.Engine.Orbit(x, y, Math.atan2(playerEngine.y - y, playerEngine.x - x) + i * 0.1, type));
//                    }
//                    lastOrbitTime = currentTime;
//                    nextOrbitTime = (long) (Math.random() * 2000) + 3000;
//                }
//
//                // 波動攻撃
//                if (currentTime - lastWaveTime > nextWaveTime) {
//                    waves.add(new toyoura.game.Engine.Wave(x, y, type));
//                    lastWaveTime = currentTime;
//                    nextWaveTime = (long) (Math.random() * 5000) + 5000;
//                }
//            }
//
//            //x,y,angle,type
//            void display(GraphicsContext gc) {
////            gc.setFill(getColor(type));
////            gc.fillOval(x - 10, y - 10, 20, 20);
////
////            gc.setFill(Color.BLACK);
////            gc.fillOval(
////                    x + Math.cos(angle) * 10 - 5,
////                    y + Math.sin(angle) * 10 - 5,
////                    10, 10
////            );
//            }
//
//            String randomType() {
//                int r = (int) (Math.random() * 3);
//                if (r == 0) return "red";
//                if (r == 1) return "green";
//                return "blue";
//            }
//
//            boolean isCollidingWith(toyoura.game.Engine.Player p) {
//                return distance(x, y, p.x, p.y) < 20 && isEffective(type, p.type);
//            }
//        }
//
//        // Attackクラス
//        class Attack {
//            double x, y, angle, range, angleRange;
//            String type;
//            long startTime;
//
//            Attack(double x, double y, double angle, String type) {
//                this.x = x;
//                this.y = y;
//                this.angle = angle;
//                this.type = type;
//                this.startTime = System.currentTimeMillis();
//                range = 100;
//                angleRange = Math.PI / 4;
//            }
//
//            void newAttack(double x, double y, double angle, String type) {
//                this.x = x;
//                this.y = y;
//                this.angle = angle;
//                this.type = type;
//                this.startTime = System.currentTimeMillis();
//            }
//
//            boolean isActive() {
//                return System.currentTimeMillis() - startTime < 500;
//            }
//
//            boolean isHit(toyoura.game.Engine.Enemy e) {
//                double angleToEnemy = angleBetween(playerEngine.x, playerEngine.y, e.x, e.y);
//                double angleDiff = fixAngle(playerEngine.angle - angleToEnemy);
//                double dist = distance(playerEngine.x, playerEngine.y, e.x, e.y);
//                return Math.abs(angleDiff) < angleRange &&
//                        dist < range &&
//                        isEffective(playerEngine.type, e.type);
//            }
//
//            //x,y,angle,type
//            void display(GraphicsContext gc) {
////            gc.setFill(getColor(type));
////            gc.fillArc(
////                    playerEngine.x - range,
////                    playerEngine.y - range,
////                    range * 2,
////                    range * 2,
////                    Math.toDegrees(-playerEngine.angle - angleRange),
////                    Math.toDegrees(angleRange * 2),
////                    javafx.scene.shape.ArcType.ROUND
////            );
//            }
//        }
//
//        // Bulletクラス
//        class Bullet {
//            double x, y, angle, speed, radius;
//            String type;
//
//            Bullet(double x, double y, double angle, String type) {
//                this.x = x;
//                this.y = y;
//                this.angle = angle;
//                this.type = type;
//                this.speed = 3;
//                this.radius = 10;
//            }
//
//            void move() {
//                x += Math.cos(angle) * speed;
//                y += Math.sin(angle) * speed;
//            }
//
//            boolean isHit(toyoura.game.Engine.Player p) {
//                return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
//            }
//
//            boolean isOutOfBounds(double w, double h) {
//                return x < 0 || x > w || y < 0 || y > h;
//            }
//
//            //x,y,type
//            void display(GraphicsContext gc) {
////            gc.setFill(getColor(type));
////            gc.fillOval(x - radius / 2, y - radius / 2, radius, radius);
//            }
//        }
//
//        // Waveクラス
//        class Wave {
//            double x, y, radius;
//            String type;
//            long startTime;
//
//            Wave(double x, double y, String type) {
//                this.x = x;
//                this.y = y;
//                this.type = type;
//                this.radius = 0;
//                this.startTime = System.currentTimeMillis();
//            }
//
//            void update() {
//                radius += 2;
//            }
//
//            boolean isActive() {
//                return System.currentTimeMillis() - startTime < 1000;
//            }
//
//            boolean isHit(toyoura.game.Engine.Player p) {
//                return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
//            }
//
//            //x,y,type
//            void display(GraphicsContext gc) {
////            gc.setStroke(getColor(type));
////            gc.setLineWidth(2);
////            gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
//            }
//        }
//
//        // Orbitクラス
//        class Orbit {
//            double x, y, radius, orbitR, orbitV, angle, angleV, anchorX, anchorY;
//            String type;
//            long startTime;
//
//            Orbit(double x, double y, double angle, String type) {
//                this.x = x;
//                this.y = y;
//                this.angle = fixAngle(angle);
//                this.type = type;
//                this.radius = 8;
//                this.orbitR = 0;
//                this.orbitV = 0.6;
//                this.angleV = 0.05;
//                this.anchorX = x;
//                this.anchorY = y;
//                this.startTime = System.currentTimeMillis();
//            }
//
//            void move() {
//                angle += angleV;
//                orbitR += orbitV;
//                x = Math.cos(angle) * orbitR + anchorX;
//                y = Math.sin(angle) * orbitR + anchorY;
//            }
//
//            boolean isActive() {
//                return System.currentTimeMillis() - startTime < 7000;
//            }
//
//            boolean isHit(toyoura.game.Engine.Player p) {
//                return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
//            }
//
//            boolean isOutOfBounds(double w, double h) {
//                return x < 0 || x > w || y < 0 || y > h;
//            }
//
//            //x,y,type
//            void display(GraphicsContext gc) {
////            gc.setFill(getColor(type));
////            gc.fillOval(x - radius / 2, y - radius / 2, radius, radius);
//            }
//        }
//
//        // メソッド
//        double angleBetween(double x1, double y1, double x2, double y2) {
//            return Math.atan2(y2 - y1, x2 - x1);
//        }
//
//        double fixAngle(double angle) {
//            while (angle <= -Math.PI) angle += 2 * Math.PI;
//            while (angle > Math.PI) angle -= 2 * Math.PI;
//            return angle;
//        }
//
//        double distance(double x1, double y1, double x2, double y2) {
//            return Math.hypot(x2 - x1, y2 - y1);
//        }
//
//        double clamp(double val, double min, double max) {
//            return Math.max(min, Math.min(max, val));
//        }
//
//        double wrap(double val, double min, double max) {
//            if (val < min) return max;
//            if (val > max) return min;
//            return val;
//        }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//    }
