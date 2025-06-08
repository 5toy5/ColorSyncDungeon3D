package toyoura.game;

import toyoura.game.ColorDetectionCam;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.scene.PerspectiveCamera;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

import org.fxyz3d.importers.obj.ObjImporter;
import org.fxyz3d.importers.Model3D;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static java.lang.Math.random;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.highgui.HighGui;

public class EngineFutureSub extends Application {
    // ゲームのグローバル変数
    Player playerEngine;
    ArrayList<Enemy> enemies;
    Attack attackEngine;
    ArrayList<Bullet> bullets;
    ArrayList<Orbit> orbits;
    ArrayList<Wave> waves;
    boolean gameOver;
    HashMap<KeyCode, Boolean> keys = new HashMap<>();
    int bgColor;

    // キャンバス
    final int width = 800;
    final int height = 600;

    long lastNanoTime;

    Snowman player = new Snowman(0, 0, 0, 10, 7, 0, "green", false);
    SphereObject sphereUnique = new SphereObject(width / 2, height / 2, 2, "red");
    ImportedObject monster = new ImportedObject(50, 10, 0, "red");
    Disc disc = new Disc(50, 10, 100, "red");
    ImportedObject monster2 = new ImportedObject(-100, -100, 0, "blue");
    ImportedObject monster3 = new ImportedObject(150, -30, 0, "green");

    int spN = 100;
    int spR, spG, spB; // 下記のsphere[]をtype別に管理するindex
    SphereObject[] sphereRed = new SphereObject[spN];
    SphereObject[] sphereGreen = new SphereObject[spN];
    SphereObject[] sphereBlue = new SphereObject[spN];

//    private double cameraarg = 0;
    private static Group root;
//    private boolean attack = false;
    private Group attackEffectGroup = new Group();
    private Group group = new Group();
//    private Group importedObject; // インポートしたオブジェクトを保持
    public PerspectiveCamera camera;

    static String colorType = "blue";

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void Engine() throws Exception {
    }

    public EngineFutureSub() throws Exception {
    }

    int p = 0;

    @Override
    public void start(Stage stage) throws Exception {
        root = new Group();
        playerEngine = new Player(width / 2, height / 2);
        attackEngine = new Attack(playerEngine.x, playerEngine.y, playerEngine.angle, playerEngine.type);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        orbits = new ArrayList<>();
        waves = new ArrayList<>();
        gameOver = false;

        for (int i = 0; i < spN; i++) {
            sphereRed[i] = new SphereObject(i * 100, i * 100, 10, "red");
            root.getChildren().add(sphereRed[i].getSphere());
        }
        for (int i = 0; i < spN; i++) {
            sphereGreen[i] = new SphereObject(i * 130, i * 130, 10, "green");
            root.getChildren().add(sphereGreen[i].getSphere());
        }
        for (int i = 0; i < spN; i++) {
            sphereBlue[i] = new SphereObject(i * 160, i * 160, 10, "blue");
            root.getChildren().add(sphereBlue[i].getSphere());
        }

        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }

        root.getChildren().add(player.getGroupFace());
        root.getChildren().add(player.getGroupRed());
        root.getChildren().add(player.getGroupGreen());
        root.getChildren().add(player.getGroupBlue());

        root.getChildren().add(player.getGroupRedAttack());
        root.getChildren().add(player.getGroupGreenAttack());
        root.getChildren().add(player.getGroupBlueAttack());

        root.getChildren().add(sphereUnique.getGroup());
        root.getChildren().add(monster.getGroupImportedObject());
        root.getChildren().add(monster2.getGroupImportedObject());
        root.getChildren().add(monster3.getGroupImportedObject());
        root.getChildren().add(disc.getGroupDisc());

        Box floor = new Board().getBoard();
        root.getChildren().add(floor);

        root.getChildren().addAll(attackEffectGroup, group);

        camera = new PerspectiveCamera(true);
        camera.setFieldOfView(45.0);
        camera.setFarClip(1000);

        Scene scene = new Scene(root, 800, 600, true);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.setTitle("JavaFX 3D Snowmen");
        stage.show();

        scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
        scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));

        lastNanoTime = System.nanoTime();

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1_000_000.0;
                lastNanoTime = currentNanoTime;

                handlePlayerInput();
                updateGame(elapsedTime);

                render();
            }
        }.start();

        startColorDetection();
    }

    void startColorDetection() {
        int cameraIndex = findCamera();
        if (cameraIndex == -1) {
            System.out.println("カメラが見つかりませんでした");
            return;
        }

        VideoCapture camera = new VideoCapture(cameraIndex);
        Mat frame = new Mat();
        double width = camera.get(Videoio.CAP_PROP_FRAME_WIDTH);
        double height = camera.get(Videoio.CAP_PROP_FRAME_HEIGHT);
        double campix = width * height;
        System.out.println("カメラ解像度: " + campix);

        new Thread(() -> {
            while (true) {
                camera.read(frame);
                if (frame.empty()) {
                    System.out.println("フレームが読み込めませんでした");
                    break;
                }

                Mat hsvImage = new Mat();
                Imgproc.cvtColor(frame, hsvImage, Imgproc.COLOR_BGR2HSV);

                Mat blueMask = new Mat();
                Mat redMask = new Mat();
                Mat greenMask = new Mat();

                Core.inRange(hsvImage, new Scalar(100, 100, 50), new Scalar(140, 255, 255), blueMask);
                Core.inRange(hsvImage, new Scalar(0, 100, 50), new Scalar(10, 255, 255), redMask);

                Mat redMask2 = new Mat();
                Core.inRange(hsvImage, new Scalar(160, 100, 50), new Scalar(180, 255, 255), redMask2);
                Core.addWeighted(redMask, 1.0, redMask2, 1.0, 0.0, redMask);

                Core.inRange(hsvImage, new Scalar(50, 100, 50), new Scalar(100, 255, 255), greenMask);

                int b = 0;
                int r = 0;
                int g = 0;

                if ((double) Core.countNonZero(blueMask) / campix > 0.125) {
                    b = Core.countNonZero(blueMask);
                }
                if ((double) Core.countNonZero(redMask) / campix > 0.125) {
                    r = Core.countNonZero(redMask);
                }
                if ((double) Core.countNonZero(greenMask) / campix > 0.125) {
                    g = Core.countNonZero(greenMask);
                }

                int maxint = Math.max(b, Math.max(r, g));
                if (b != 0 && Core.countNonZero(blueMask) == maxint) {
                    System.out.println("青色検出!");
                    colorType = "blue";
                }
                if (r != 0 && Core.countNonZero(redMask) == maxint) {
                    System.out.println("赤色検出!");
                    colorType = "red";
                }
                if (g != 0 && Core.countNonZero(greenMask) == maxint) {
                    System.out.println("緑色検出!");
                    colorType = "green";
                }

                HighGui.imshow("Camera Feed", frame);
                if (HighGui.waitKey(1) == 27) {
                    break;
                }
            }

            camera.release();
            HighGui.destroyAllWindows();
        }).start();
    }

    public static int findCamera() {
        for (int i = 0; i < 10; i++) {
            VideoCapture camera = new VideoCapture(i);
            if (camera.isOpened()) {
                camera.release();
                return i;
            }
        }
        return -1;
    }

    void handlePlayerInput() {
        if (isKeyPressed(KeyCode.W)) playerEngine.move(0);
        if (isKeyPressed(KeyCode.S)) playerEngine.move(Math.PI);
        if (isKeyPressed(KeyCode.A)) playerEngine.move(-Math.PI / 2);
        if (isKeyPressed(KeyCode.D)) playerEngine.move(Math.PI / 2);

        if (isKeyPressed(KeyCode.Q)) playerEngine.rotate(-0.1);
        if (isKeyPressed(KeyCode.E)) playerEngine.rotate(0.1);

        // ColorDetectionCam.colorType を使用してプレイヤーのタイプを変更
        playerEngine.changeType(colorType);

        // テスト用
        if (isKeyPressed(KeyCode.SPACE)) {
            player.changeAttack();
        }
        if (isKeyPressed(KeyCode.DIGIT7)) {
            disc.clearDisc();
            monster.clear();
        }
        if (isKeyPressed(KeyCode.DIGIT0)) {
            player.setAttackObjectScale(1.01, 1.01, 1.01);
        }
        if (isKeyPressed(KeyCode.DIGIT8)) {
            disc.setDiscScale(100);
        }
        if (isKeyPressed(KeyCode.DIGIT9)) {
            disc.setDiscScale(50);
        }
        if (isKeyPressed(KeyCode.SPACE)) {
            playerEngine.attackEngine(attackEngine);
        }
    }

    void updateGame(double elapsedTime) {
        if (gameOver) return;

        //敵の更新
        for (Enemy enemy : enemies) {
            enemy.move();
            enemy.attackEngine();
        }

        // 攻撃の更新
        if (attackEngine.isActive()) {
            for (Enemy enemy : enemies) {
                if (attackEngine.isHit(enemy)) {
                    enemy.respawn();
                }
            }
        }

        // 銃弾の更新
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.move();
            if (bullet.isActive()) {
                bulletIter.remove();
            } else if (bullet.isHit(playerEngine)) {
                bulletIter.remove();
                playerEngine.damaged();
            }
        }

        sphereUnique.setX(sphereUnique.x + 1);
        sphereUnique.setY(sphereUnique.y + 1);
        sphereUnique.update();

        // 軌道弾の更新
        Iterator<Orbit> orbitIter = orbits.iterator();
        while (orbitIter.hasNext()) {
            Orbit orbit = orbitIter.next();
            if (orbit.isActive()) {
                orbit.move();
                if (orbit.isOutOfBounds(width, height)) {
                    orbitIter.remove();
                } else if (orbit.isHit(playerEngine)) {
                    orbitIter.remove();
                    playerEngine.damaged();
                }
            } else {
                orbitIter.remove();
            }
        }

        // 波動の更新
        Iterator<Wave> waveIter = waves.iterator();
        while (waveIter.hasNext()) {
            Wave wave = waveIter.next();
            if (wave.isActive()) {
                wave.update();
                if (wave.isHit(playerEngine)) {
                    playerEngine.damaged();
                }
            } else {
                waveIter.remove();
            }
        }

        // 敵との衝突判定
        for (Enemy enemy : enemies) {
            if (enemy.isCollidingWith(playerEngine)) {
                enemy.respawn();
                playerEngine.damaged();
            }
        }
    }

    void render() {
//        //キャンバスのリセット
//        gc.setFill(Color.rgb(bgColor, bgColor, bgColor));
//        gc.fillRect(0, 0, width, height);

        //ゲームオーバー
        if (gameOver) {
//            gc.setFill(Color.WHITE);
//            gc.fillText("Game Over", width / 2 - 50, height / 2);
            return;
        }

        // プレイヤーの描画
        playerEngine.display();

        // 敵の描画
        for (Enemy enemy : enemies) {
            enemy.display();
        }

        // 攻撃の描画
        if (attackEngine.isActive()) {
            attackEngine.display();
        }

//        p++;
//        for(int i = 0; i < spN; i++){
//            sphereRed[i].setX(p*5);
//            sphereRed[i].setY(p*5);
//            sphereGreen[i].setX(p*5);
//            sphereGreen[i].setY(p*5);
//            sphereBlue[i].setX(p*5);
//            sphereBlue[i].setY(p*5);
//        }
        spR=0;spG=0;spB=0;
        // 銃弾の描画
        for (Bullet bullet : bullets) {
            switch (bullet.type) {
                case "red":
                    sphereRed[spR].setX(bullet.x);
                    sphereRed[spR].setY(bullet.y);
                    if(spR<spN)
                        spR++;
                case "green":
                    sphereGreen[spG].setX(bullet.x);
                    sphereGreen[spG].setY(bullet.y);
                    if(spG<spN)
                        spG++;
                case "blue":
                    sphereBlue[spB].setX(bullet.x);
                    sphereBlue[spB].setY(bullet.y);
                    if(spB<spN)
                        spB++;
            }
        }

        // 軌道弾の描画
        for (Orbit orbit : orbits) {
            switch (orbit.type) {
                case "red":
                    sphereRed[spR].setX(orbit.x);
                    sphereRed[spR].setY(orbit.y);
                    if(spR<spN)
                        spR++;
                case "green":
                    sphereGreen[spG].setX(orbit.x);
                    sphereGreen[spG].setY(orbit.y);
                    if(spG<spN)
                        spG++;
                case "blue":
                    sphereBlue[spB].setX(orbit.x);
                    sphereBlue[spB].setY(orbit.y);
                    if(spB<spN)
                        spB++;
            }
        }

        // 波動の描画
//        for (Wave wave : waves) {
//            switch (wave.type) {
//                case "red":
//                    sphereRed[spR].setX(wave.x);
//                    sphereRed[spR].setY(wave.y);
//                    if(spR<spN)
//                        spR++;
//                case "green":
//                    sphereGreen[spG].setX(wave.x);
//                    sphereGreen[spG].setY(wave.y);
//                    if(spG<spN)
//                        spG++;
//                case "blue":
//                    sphereBlue[spB].setX(wave.x);
//                    sphereBlue[spB].setY(wave.y);
//                    if(spB<spN)
//                        spB++;
//            }
//        }

        for (;spR<spN;spR++){
            sphereRed[spR].clear();
        }
        for (;spG<spN;spG++){
            sphereGreen[spG].clear();
        }
        for (;spB<spN;spB++){
            sphereBlue[spB].clear();
        }

        for (int i = 0; i < spN; i++) {
            sphereRed[i].update();
            sphereGreen[i].update();
            sphereBlue[i].update();
        }



        // カメラの位置と回転を更新
        double snowmanCenterX = player.getCenterX();
        double snowmanCenterY = player.getCenterY();
        double snowmanCenterZ = player.getCenterZ();

        camera.getTransforms().setAll(
                new Rotate(player.rotate, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Z_AXIS),
                new Rotate(0, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.Y_AXIS),
                new Rotate(60, snowmanCenterX, snowmanCenterY, snowmanCenterZ, Rotate.X_AXIS),
                new Translate(player.x, player.y, -100)
        );
    }

    boolean isKeyPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    Color getColor(String type) {
        switch (type) {
            case "red":
                return Color.RED;
            case "green":
                return Color.LIME;
            case "blue":
                return Color.BLUE;
            default:
                return Color.BLACK;
        }
    }

//    boolean isEffective(String attackEngineType, String targetType) {
//        return (attackEngineType.equals("red") && targetType.equals("green")) ||
//                (attackEngineType.equals("green") && targetType.equals("blue")) ||
//                (attackEngineType.equals("blue") && targetType.equals("red"));
//    }

    boolean isEffective(String attackEngineType, String targetType) {
        if (targetType == null) {
            return false;
        }
        return (attackEngineType.equals("red") && targetType.equals("green")) ||
                (attackEngineType.equals("green") && targetType.equals("blue")) ||
                (attackEngineType.equals("blue") && targetType.equals("red"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Playerクラス
    class Player {
        double x, y, angle, speed;
        String type;
        int hp;
        long lastDamagedTime;

        Player(double x, double y) {
            this.x = x;
            this.y = y;
            this.angle = 1;
            this.speed = 3;
            this.hp = 10;
            this.type = "red";
            this.lastDamagedTime = System.currentTimeMillis();
        }

        void move(double da) {
            x += Math.cos(angle + da) * speed;
            y += Math.sin(angle + da) * speed;
//            x += Math.cos(angle + da) * speed;
//            y += Math.sin(angle + da) * speed;
//            x = clamp(x, 0, width);
//            y = clamp(y, 0, height);
        }

        void rotate(double da) {
            angle += da;
        }

        void changeType(String newType) {
            type = newType;
            player.setType(newType);
            player.update();
        }

        void attackEngine(Attack a) {
            a.newAttack(x, y, angle, type);
        }

        void damaged() {
            if (System.currentTimeMillis() - lastDamagedTime > 200) {
                hp -= 1;
                if (hp <= 0) gameOver = true;
                lastDamagedTime = System.currentTimeMillis();
            }
        }

        //x,y,angle,type
        void display() {
            player.setX(x);
            player.setY(y);
//            player.setRotate(Math.toDegrees(angle + Math.PI / 2));
            player.setRotate(Math.toDegrees(angle + Math.PI /2));
            player.update();
//            bgColor = 25 * hp;
//
//            gc.setFill(getColor(type));
//            gc.fillOval(x - 15, y - 15, 30, 30);
//
//            gc.setFill(Color.WHITE);
//            gc.fillOval(
//                    x + Math.cos(angle) * 10 - 5,
//                    y + Math.sin(angle) * 10 - 5,
//                    10, 10
//            );
//
        }
    }

    // Enemyクラス
    class Enemy {
        double x, y, angle, speed, da;
        String type;
        long lastBulletTime, nextBulletTime;
        long lastOrbitTime, nextOrbitTime;
        long lastWaveTime, nextWaveTime;

        Enemy() {
            respawn();
            long currentTime = System.currentTimeMillis();
            lastBulletTime = currentTime;
            lastOrbitTime = currentTime;
            lastWaveTime = currentTime;
        }

        void respawn() {
            x = random() * width;
            y = random() * height;
            angle = random() * (2 * Math.PI) - Math.PI;
            speed = 1;
            type = randomType();
        }

        void move() {
            angle += da;
            x += Math.cos(angle) * speed;
            y += Math.sin(angle) * speed;
            x = wrap(x, 0, width);
            y = wrap(y, 0, height);
        }

        void attackEngine() {
            long currentTime = System.currentTimeMillis();

            // 銃弾攻撃
            if (currentTime - lastBulletTime > nextBulletTime) {
                angle = Math.atan2(playerEngine.y - y, playerEngine.x - x);
                da = random() * (Math.PI / 25) - (Math.PI / 50);
                bullets.add(new Bullet(x, y, angle, type));
                lastBulletTime = currentTime;
                nextBulletTime = (long) (random() * 2000) + 1000;
            }

            // 軌道弾攻撃
            if (currentTime - lastOrbitTime > nextOrbitTime) {
                for (int i = 0; i < 10; i++) {
                    orbits.add(new Orbit(x, y, Math.atan2(playerEngine.y - y, playerEngine.x - x) + i * 0.1, type));
                }
                lastOrbitTime = currentTime;
                nextOrbitTime = (long) (random() * 2000) + 3000;
            }

            // 波動攻撃
            if (currentTime - lastWaveTime > nextWaveTime) {
                waves.add(new Wave(x, y, type));
                lastWaveTime = currentTime;
                nextWaveTime = (long) (random() * 5000) + 5000;
            }
        }

        //x,y,angle,type
        void display() {
//            gc.setFill(getColor(type));
//            gc.fillOval(x - 10, y - 10, 20, 20);
//
//            gc.setFill(Color.BLACK);
//            gc.fillOval(
//                    x + Math.cos(angle) * 10 - 5,
//                    y + Math.sin(angle) * 10 - 5,
//                    10, 10
//            );
        }

        String randomType() {
            int r = (int) (random() * 3);
            if (r == 0) return "red";
            if (r == 1) return "green";
            return "blue";
        }

        boolean isCollidingWith(Player p) {
            return distance(x, y, p.x, p.y) < 20 && isEffective(type, p.type);
        }
    }

    // Attackクラス
    class Attack {
        double x, y, angle, range, angleRange;
        String type;
        long startTime;

        Attack(double x, double y, double angle, String type) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.type = type;
            this.startTime = System.currentTimeMillis();
            range = 100;
            angleRange = Math.PI / 4;
        }

        void newAttack(double x, double y, double angle, String type) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.type = type;
            this.startTime = System.currentTimeMillis();
        }

        boolean isActive() {
            return System.currentTimeMillis() - startTime < 500;
        }

        boolean isHit(Enemy e) {
            double angleToEnemy = angleBetween(playerEngine.x, playerEngine.y, e.x, e.y);
            double angleDiff = fixAngle(playerEngine.angle - angleToEnemy);
            double dist = distance(playerEngine.x, playerEngine.y, e.x, e.y);
            return Math.abs(angleDiff) < angleRange &&
                    dist < range &&
                    isEffective(playerEngine.type, e.type);
        }

        //x,y,angle,type
        void display() {
//            gc.setFill(getColor(type));
//            gc.fillArc(
//                    playerEngine.x - range,
//                    playerEngine.y - range,
//                    range * 2,
//                    range * 2,
//                    Math.toDegrees(-playerEngine.angle - angleRange),
//                    Math.toDegrees(angleRange * 2),
//                    javafx.scene.shape.ArcType.ROUND
//            );
        }
    }

    // Bulletクラス
    class Bullet {
        double x, y, angle, speed, radius;
        String type;
        long startTime;


        Bullet(double x, double y, double angle, String type) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.type = type;
            this.speed = 3;
            this.radius = 10;
            this.startTime = System.currentTimeMillis();
        }

        void move() {
            x += Math.cos(fixAngle(angle)) * speed;
            y += Math.sin(fixAngle(angle)) * speed;
        }

        boolean isHit(Player p) {
            return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
        }


        boolean isActive() {
            return System.currentTimeMillis() - startTime < 10000;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public String getType() {
            return type;
        }

//        //x,y,type
//        void display() {
//            sphere.setX(x);
//            sphere.setY(y);
//            Platform.runLater(() -> sphere.update());
////            sphere.update();
//        }
    }

    // Waveクラス
    class Wave {
        double x, y, radius;
        String type;
        long startTime;

        Wave(double x, double y, String type) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.radius = 0;
            this.startTime = System.currentTimeMillis();
        }

        void update() {
            radius += 2;
        }

        boolean isActive() {
            return System.currentTimeMillis() - startTime < 1000;
        }

        boolean isHit(Player p) {
            return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
        }

        //x,y,type
        void display() {
//            gc.setStroke(getColor(type));
//            gc.setLineWidth(2);
//            gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    // Orbitクラス
    class Orbit {
        double x, y, radius, orbitR, orbitV, angle, angleV, anchorX, anchorY;
        String type;
        long startTime;

        Orbit(double x, double y, double angle, String type) {
            this.x = x;
            this.y = y;
            this.angle = fixAngle(angle);
            this.type = type;
            this.radius = 8;
            this.orbitR = 0;
            this.orbitV = 0.6;
            this.angleV = 0.05;
            this.anchorX = x;
            this.anchorY = y;
            this.startTime = System.currentTimeMillis();
        }

        void move() {
            angle += angleV;
            orbitR += orbitV;
            x = Math.cos(angle) * orbitR + anchorX;
            y = Math.sin(angle) * orbitR + anchorY;
        }

        boolean isActive() {
            return System.currentTimeMillis() - startTime < 7000;
        }

        boolean isHit(Player p) {
            return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
        }

        boolean isOutOfBounds(double w, double h) {
            return x < 0 || x > w || y < 0 || y > h;
        }

        //x,y,type
        void display() {
//            gc.setFill(getColor(type));
//            gc.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        }
    }

    public class Board {
        private Box board;
        private double width = 10000;
        private double height = 10000;
        private double depth = 1;
        private double translateZ = 10;
        private Color color = Color.rgb(194, 178, 128);

        public Board() {
            board = new Box(width, height, depth);
            board.setTranslateZ(translateZ);
            board.setMaterial(new PhongMaterial(color));
        }

        public Box getBoard() {
            return board;
        }
    }

    static class Snowman {
        double x, y, zRed, zGreen, zBlue, rotate;
        double redAttackZ, greenAttackZ, blueAttackZ;
        double showZ = 0;
        double clearZ = 50;
        double baseRadius, topRadius, offsetY;
        double attackSize = 1.0; //攻撃の扇形の半径=100*attackSizeで計算;
        boolean isAttack;
        String str;
        Group groupFace;
        Group groupRed;
        Group groupGreen;
        Group groupBlue;
        Group groupRedAttack, groupGreenAttack, groupBlueAttack;
        File fileRed, fileGreen, fileBlue;

        Snowman(double x, double y, double rotate, double baseRadius, double topRadius, double offsetY, String str, boolean isAttack) throws Exception {
            this.x = x;
            this.y = y;
//            this.zRed = showZ;
//            this.zGreen = clearZ;
//            this.zBlue = clearZ;
//            if(Objects.equals(this.str, "red")) {zRed = showZ; zGreen = clearZ; zBlue = clearZ;}
//            if(Objects.equals(this.str, "green")) {zRed = clearZ; zGreen = showZ; zBlue = clearZ;}
//            else {zRed = clearZ; zGreen = clearZ; zBlue = showZ;}
            this.rotate = rotate;
            this.baseRadius = baseRadius;
            this.topRadius = topRadius;
            this.offsetY = offsetY;
            this.str = str;
            this.isAttack = isAttack;

            // グループの初期化
            this.groupRedAttack = new Group();
            this.groupGreenAttack = new Group();
            this.groupBlueAttack = new Group();

            drawSnowman();
        }

        void drawSnowman() throws Exception {
            Group newGroupFace;
            Group newGroupRed;
            Group newGroupGreen;
            Group newGroupBlue;
            Sphere baseRed = new Sphere(baseRadius); Sphere baseGreen = new Sphere(baseRadius); Sphere baseBlue = new Sphere(baseRadius);
            Sphere topRed = new Sphere(topRadius); Sphere topGreen = new Sphere(topRadius); Sphere topBlue = new Sphere(topRadius);
            baseRed.setMaterial(new PhongMaterial(Color.RED));
            topRed.setMaterial(new PhongMaterial(Color.RED));
            baseGreen.setMaterial(new PhongMaterial(Color.GREEN));
            topGreen.setMaterial(new PhongMaterial(Color.GREEN));
            baseBlue.setMaterial(new PhongMaterial(Color.BLUE));
            topBlue.setMaterial(new PhongMaterial(Color.BLUE));

//            if(Objects.equals(this.str, "red")) {zRed = showZ; zGreen = clearZ; zBlue = clearZ;}
//            else if(Objects.equals(this.str, "green")) {zRed = clearZ; zGreen = showZ; zBlue = clearZ;}
//            else {zRed = clearZ; zGreen = clearZ; zBlue = showZ;}

            baseRed.getTransforms().add(new Translate(0,0,zRed));
            baseGreen.getTransforms().add(new Translate(0,0,zGreen));
            baseBlue.getTransforms().add(new Translate(0,0,zBlue));
            topRed.getTransforms().add(new Translate(0, 0, zRed-17));
            topGreen.getTransforms().add(new Translate(0, 0, zGreen-17));
            topBlue.getTransforms().add(new Translate(0, 0, zBlue-17));

            Sphere eye1 = new Sphere(1);
            eye1.setMaterial(new PhongMaterial(Color.BLACK));
            eye1.getTransforms().add(new Translate(-2, -6, -20));

            Sphere eye2 = new Sphere(1);
            eye2.setMaterial(new PhongMaterial(Color.BLACK));
            eye2.getTransforms().add(new Translate(2, -6, -20));

            Cylinder nose = new Cylinder(0.5, 5);
            nose.setMaterial(new PhongMaterial(Color.ORANGE));
            nose.getTransforms().add(new Translate(0, -6, -17));

            Cylinder leftArm = new Cylinder(0.5, 10);
            leftArm.setMaterial(new PhongMaterial(Color.BLACK));
            leftArm.getTransforms().add(new Translate(-10, 0, -10));
            leftArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));

            Cylinder rightArm = new Cylinder(0.5, 10);
            rightArm.setMaterial(new PhongMaterial(Color.BLACK));
            rightArm.getTransforms().add(new Translate(10, 0, -10));
            rightArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));

            createAttackObject();

            newGroupFace = new Group(eye1, eye2, nose, leftArm, rightArm);
            newGroupRed = new Group(baseRed, topRed);
            newGroupGreen = new Group(baseGreen, topGreen);
            newGroupBlue = new Group(baseBlue, topBlue);

            groupFace = newGroupFace;
            groupRed = newGroupRed;
            groupGreen = newGroupGreen;
            groupBlue = newGroupBlue;

            groupRed.getTransforms().add(new Translate(0, offsetY, 0));
            groupGreen.getTransforms().add(new Translate(0, offsetY, 0));
            groupBlue.getTransforms().add(new Translate(0, offsetY, 0));

            groupRedAttack.getTransforms().add(new Translate(0, offsetY, 0));
            groupGreenAttack.getTransforms().add(new Translate(0, offsetY, 0));
            groupBlueAttack.getTransforms().add(new Translate(0, offsetY, 0));
        }

        // Snowmanクラスにインポートオブジェクトを読み込むメソッドを追加
        public void createAttackObject() throws Exception {
            // 赤い攻撃オブジェクトを読み込む
            fileRed = new File("src/main/resources/3dmodels/redAttackRadius100.obj");
            ObjImporter importerRed = new ObjImporter();
            Model3D modelRed = importerRed.load(fileRed.toURI().toURL());
            Group importedRedObject = modelRed.getRoot();
            importedRedObject.getTransforms().add(new Scale(attackSize, attackSize, 1.0));
            groupRedAttack.getChildren().add(importedRedObject);

            // 緑の攻撃オブジェクトを読み込む
            fileGreen = new File("src/main/resources/3dmodels/greenAttackRadius100.obj");
            ObjImporter importerGreen = new ObjImporter();
            Model3D modelGreen = importerGreen.load(fileGreen.toURI().toURL());
            Group importedGreenObject = modelGreen.getRoot();
            importedGreenObject.getTransforms().add(new Scale(attackSize, attackSize, 1.0));
            groupGreenAttack.getChildren().add(importedGreenObject);

            // 青い攻撃オブジェクトを読み込む
            fileBlue = new File("src/main/resources/3dmodels/blueAttackRadius100.obj");
            ObjImporter importerBlue = new ObjImporter();
            Model3D modelBlue = importerBlue.load(fileBlue.toURI().toURL());
            Group importedBlueObject = modelBlue.getRoot();
            importedBlueObject.getTransforms().add(new Scale(attackSize, attackSize, 1.0));
            groupBlueAttack.getChildren().add(importedBlueObject);
        }

        public void setAttackObjectScale(double scaleX, double scaleY, double scaleZ) {
            for (Node node : groupRedAttack.getChildren()) {
                if (node instanceof Group) {
                    node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
                }
            }
            for (Node node : groupGreenAttack.getChildren()) {
                if (node instanceof Group) {
                    node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
                }
            }
            for (Node node : groupBlueAttack.getChildren()) {
                if (node instanceof Group) {
                    node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
                }
            }
        }

//        public void setAttackObjectScale(double scaleX, double scaleY, double scaleZ) {
//            for (Node node : groupRed.getChildren()) {
//                if (node instanceof Group) {
//                    node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
//                }
//            }
//        }


//        public void updateAttackObjectPosition() {
//
//            if (isAttack) {
//                if (Objects.equals(str, "red")) {
//                    redAttackZ = showZ; greenAttackZ = clearZ; blueAttackZ =clearZ;
//                } else if (Objects.equals(str, "green")) {
//                    greenAttackZ = showZ; redAttackZ = clearZ; blueAttackZ = clearZ;
//                } else if (Objects.equals(str, "blue")) {
//                    blueAttackZ = showZ; redAttackZ = clearZ; greenAttackZ = clearZ;
//                }
//            }
//            else{
//                redAttackZ = clearZ; greenAttackZ = clearZ; blueAttackZ =clearZ;
//            }
//
//            this.groupRedAttack.getTransforms().setAll(
//                    new Translate(x, y, redAttackZ),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//            this.groupGreenAttack.getTransforms().setAll(
//                    new Translate(x, y, greenAttackZ),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//            this.groupBlueAttack.getTransforms().setAll(
//                    new Translate(x, y, blueAttackZ),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//        }

        public Group getGroupFace() {
            return groupFace;
        }

        public Group getGroupRed() {
            return groupRed;
        }

        public Group getGroupGreen() {
            return groupGreen;
        }

        public Group getGroupBlue() {
            return groupBlue;
        }

        public Group getGroupRedAttack() {
            return groupRedAttack;
        }

        public Group getGroupGreenAttack() {
            return groupGreenAttack;
        }

        public Group getGroupBlueAttack() {
            return groupBlueAttack;
        }

        public double getCenterX() {
            return this.x;
        }

        public double getCenterY() {
            return this.y;
        }

        public double getCenterZ() {

            return showZ;
//            if(Objects.equals(this.str, "red")) return groupRed.getBoundsInParent().getCenterZ();
//            else if(Objects.equals(this.str, "green")) return groupGreen.getBoundsInParent().getCenterZ();
//            else return groupBlue.getBoundsInParent().getCenterZ();

        }

        void changeAttack() {
            this.isAttack = !this.isAttack;
        }

        void setX(double newX) {
            this.x = newX;
        }

        void setY(double newY) {
            this.y = newY;
        }

        void setRotate(double newR) {
            this.rotate = newR;
        }

        void setType(String newType) {
            this.str = newType;
        }

        void update() {
            if(Objects.equals(this.str, "red")) {
                zRed = showZ; zGreen = clearZ; zBlue = clearZ;
            }
            else if(Objects.equals(this.str, "green")) {
                zRed = clearZ; zGreen = showZ; zBlue = clearZ;
            }
            else {
                zRed = clearZ; zGreen = clearZ; zBlue = showZ;
            }

            if (isAttack) {
                if (Objects.equals(str, "red")) {
                    redAttackZ = showZ; greenAttackZ = clearZ; blueAttackZ =clearZ;
                } else if (Objects.equals(str, "green")) {
                    greenAttackZ = showZ; redAttackZ = clearZ; blueAttackZ = clearZ;
                } else if (Objects.equals(str, "blue")) {
                    blueAttackZ = showZ; redAttackZ = clearZ; greenAttackZ = clearZ;
                }
            }
            else{
                redAttackZ = clearZ; greenAttackZ = clearZ; blueAttackZ =clearZ;
            }

            this.groupFace.getTransforms().setAll(
                    new Translate(x, y, showZ),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );

            this.groupRed.getTransforms().setAll(
                    new Translate(x, y, zRed),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );
            this.groupGreen.getTransforms().setAll(
                    new Translate(x, y, zGreen),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );
            this.groupBlue.getTransforms().setAll(
                    new Translate(x, y, zBlue),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );

            this.groupRedAttack.getTransforms().setAll(
                    new Translate(x, y, redAttackZ),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );
            this.groupGreenAttack.getTransforms().setAll(
                    new Translate(x, y, greenAttackZ),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );
            this.groupBlueAttack.getTransforms().setAll(
                    new Translate(x, y, blueAttackZ),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );

//            this.groupRedAttack.getTransforms().setAll(
//                    new Translate(x, y, zRedAttack),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//            this.groupGreenAttack.getTransforms().setAll(
//                    new Translate(x, y, zGreenAttack),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
//            this.groupBlue.getTransforms().setAll(
//                    new Translate(x, y, zBlueAttack),
//                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
//            );
        }
    }

    static class SphereObject {
        double x, y, radius;
        String str;
        Sphere sphere;  // Sphereを直接保持する
        Group group;

        SphereObject(double x, double y, double radius, String str) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.str = str;

            sphere = createSphere();
            group = new Group(sphere);
        }

        private Sphere createSphere() {
            Sphere sphere = new Sphere(radius);
            if (Objects.equals(str, "red")) {
                sphere.setMaterial(new PhongMaterial(Color.RED));
            } else if (Objects.equals(str, "green")) {
                sphere.setMaterial(new PhongMaterial(Color.GREEN));
            } else if (Objects.equals(str, "blue")) {
                sphere.setMaterial(new PhongMaterial(Color.BLUE));
            }
            sphere.getTransforms().add(new Translate(x, y, 0));
            return sphere;
        }

        public Group getGroup() {
            return group;
        }

        void setX(double newX) {
            this.x = newX;
        }

        void setY(double newY) {
            this.y = newY;
        }

        void update() {
            sphere.getTransforms().setAll(new Translate(x, y, 0));
        }

        void clear() {
            this.x = 10000; this.y = 10000;
        }

        public Node getSphere() {
            return sphere;
        }
    }

//    static class SphereObject {
//        double x, y, radius;
//        String str;
//        Sphere sphere;  // Sphereを直接保持する
//        Group group;
//
//        SphereObject(double x, double y, double radius, String str) {
//            this.x = x;
//            this.y = y;
//            this.radius = radius;
//            this.str = str;
//
//            sphere = createSphere();
//            group = new Group(sphere);
//        }
//
//        private Sphere createSphere() {
//            Sphere sphere = new Sphere(radius);
//            if (Objects.equals(str, "red")) {
//                sphere.setMaterial(new PhongMaterial(Color.RED));
//            } else if (Objects.equals(str, "green")) {
//                sphere.setMaterial(new PhongMaterial(Color.GREEN));
//            } else if (Objects.equals(str, "blue")) {
//                sphere.setMaterial(new PhongMaterial(Color.BLUE));
//            }
//            sphere.getTransforms().add(new Translate(x, y, 0));
//            return sphere;
//        }
//
//        public Group getGroup() {
//            return group;
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
//        void update() {
//            sphere.getTransforms().add(new Translate(x, y, 0));
//            group = new Group(sphere);
//        }
//
//        void clear() {
//            group.getChildren().clear();
//        }
//
//        public Node getSphere() {
//            return sphere;
//        }
//    }

    static class ImportedObject {
        double x, y, z, rotate;
        String str;
        Group group;
        File file;

        ImportedObject(double x, double y, double rotate, String str) throws Exception {
            this.x = x;
            this.y = y;
            this.z = 10;
            this.rotate = rotate;
            this.str = str;
            loadImportedObject();
            update();
        }

        void loadImportedObject() throws Exception {
            if (Objects.equals(str, "red")) file = new File("src/main/resources/3dmodels/redMonster.obj");
            if (Objects.equals(str, "green")) file = new File("src/main/resources/3dmodels/greenMonster.obj");
            else if (Objects.equals(str, "blue")) file = new File("src/main/resources/3dmodels/blueMonster.obj");
            ObjImporter importer = new ObjImporter();
            Model3D model = importer.load(file.toURI().toURL());
            group = model.getRoot();
        }

//        public void clearObject() {
//            group.getChildren().clear();
//        }

        public Group getGroupImportedObject() {
            return group;
        }

        void setX(double newX) {
            this.x = newX;
        }

        void setY(double newY) {
            this.y = newY;
        }

        void clear() {
            this.x = 10000; this.y = 10000;
        }

        void update() {
            this.group.getTransforms().setAll(
                    new Translate(x, y, z),
                    new Rotate(180, 0, 0, 0, Rotate.X_AXIS),
                    new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
            );
        }
    }

    static class Disc {
        double x, y, z, radius;
        String str;
        Group group;

        public Disc(double x, double y, double radius, String str) {
            this.x = x;
            this.y = y;
            this.z = -2;
            this.radius = radius;
            this.str = str;
            createDisc();
        }

        public void createDisc() {
            double discRadius = radius;
            double discHeight = 1;
            Cylinder disc = new Cylinder(discRadius, discHeight);
            if (Objects.equals(str, "red")) disc.setMaterial(new PhongMaterial(Color.RED));
            else if (Objects.equals(str, "green")) disc.setMaterial(new PhongMaterial(Color.GREEN));
            else if (Objects.equals(str, "blue")) disc.setMaterial(new PhongMaterial(Color.BLUE));
            double centerX = x;
            double centerY = y;
            double centerZ = z;
            disc.getTransforms().addAll(
                    new Rotate(90, Rotate.X_AXIS),
                    new Translate(centerX, centerY - 3, centerZ)
            );

            group = new Group(disc);
        }

        public void clearDisc() {
            group.getChildren().clear();
        }

        public void clear(){
            this.x = 10000; this.y = 10000;
        }

//        public void setDiscScale(double scaleX, double scaleY, double scaleZ) {
//            for (Node node : group.getChildren()) {
//                if (node instanceof Cylinder) {
//                    node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
//                }
//            }
//        }

        public void setDiscScale(double newRadius) {
            double scaleFactor = newRadius / this.radius;
            for (Node node : group.getChildren()) {
                if (node instanceof Cylinder) {
                    node.getTransforms().add(new Scale(scaleFactor, 1, scaleFactor));
                }
            }
            this.radius = newRadius; // Update the current radius to the new radius
        }

        public Group getGroupDisc() {
            return group;
        }
    }


    // メソッド
    double angleBetween(double x1, double y1, double x2, double y2) {
        return Math.atan2(y2 - y1, x2 - x1);
    }

    double fixAngle(double angle) {
        while (angle <= -Math.PI) angle += 2 * Math.PI;
        while (angle > Math.PI) angle -= 2 * Math.PI;
        return angle;
    }

    double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    double wrap(double val, double min, double max) {
        if (val < min) return max;
        if (val > max) return min;
        return val;
    }
}