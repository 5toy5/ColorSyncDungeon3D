package MVC.game.view;

import MVC.game.model.*;
import MVC.game.model3D.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.scene.PerspectiveCamera;
import javafx.stage.Stage;
import toyoura.game.EngineFutureSub;

import java.util.List;

public class GameView3D {
    private Group root;
    Scene scene;
    private PerspectiveCamera camera;
    Snowman playerSnowman = new Snowman(0, 0, 0, 10, 7, 0, "green", false);

    int enN = 20;
    int enR, enG, enB; // 下記のmonster[]をtype別に管理するindex
    ImportedObject[] monsterRed = new ImportedObject[enN];
    ImportedObject[] monsterGreen = new ImportedObject[enN];
    ImportedObject[] monsterBlue = new ImportedObject[enN];

    int spN = 100;
    int spR, spG, spB; // 下記のsphere[]をtype別に管理するindex
    SphereObject[] spRed = new SphereObject[spN];
    SphereObject[] spGreen = new SphereObject[spN];
    SphereObject[] spBlue = new SphereObject[spN];

    int obN = 100;
    int obR, obG, obB; // 下記のob[]をtype別に管理するindex
    SphereObject[] obRed = new SphereObject[obN];
    SphereObject[] obGreen = new SphereObject[obN];
    SphereObject[] obBlue = new SphereObject[obN];

    int waN = 100;
    int waR, waG, waB; // 下記のwa[]をtype別に管理するindex
    Disc[] waRed = new Disc[waN];
    Disc[] waGreen = new Disc[waN];
    Disc[] waBlue = new Disc[waN];

    int allRadius = 7;

    public GameView3D(Stage stage) throws Exception {
        root = new Group();
        root.getChildren().add(playerSnowman.getGroupFace());
        root.getChildren().add(playerSnowman.getGroupRed());
        root.getChildren().add(playerSnowman.getGroupGreen());
        root.getChildren().add(playerSnowman.getGroupBlue());
        root.getChildren().add(playerSnowman.getGroupRedAttack());
        root.getChildren().add(playerSnowman.getGroupGreenAttack());
        root.getChildren().add(playerSnowman.getGroupBlueAttack());

        for (int i = 0; i < enN; i++) {
            monsterRed[i] = new ImportedObject(0, 0, 0, "red");
            root.getChildren().add(monsterRed[i].getGroupImportedObject());
            monsterRed[i].clear();
        }
        for (int i = 0; i < enN; i++) {
            monsterGreen[i] = new ImportedObject(0, 0, 0, "green");
            root.getChildren().add(monsterGreen[i].getGroupImportedObject());
            monsterGreen[i].clear();
        }
        for (int i = 0; i < enN; i++) {
            monsterBlue[i] = new ImportedObject(0, 0, 0, "blue");
            root.getChildren().add(monsterBlue[i].getGroupImportedObject());
            monsterBlue[i].clear();
        }

        //sphere
        for (int i = 0; i < spN; i++) {
            spRed[i] = new SphereObject(0, 0, allRadius, "red");
            root.getChildren().add(spRed[i].getGroup());
            spRed[i].clear();
        }
        for (int i = 0; i < spN; i++) {
            spGreen[i] = new SphereObject(0, 0, allRadius, "green");
            root.getChildren().add(spGreen[i].getGroup());
            spGreen[i].clear();
        }
        for (int i = 0; i < spN; i++) {
            spBlue[i] = new SphereObject(0, 0, allRadius, "blue");
            root.getChildren().add(spBlue[i].getGroup());
            spBlue[i].clear();
        }

        //おーびっと
        for (int i = 0; i < obN; i++) {
            obRed[i] = new SphereObject(0, 0, allRadius, "red");
            root.getChildren().add(obRed[i].getGroup());
            obRed[i].clear();
        }
        for (int i = 0; i < obN; i++) {
            obGreen[i] = new SphereObject(0, 0, allRadius, "green");
            root.getChildren().add(obGreen[i].getGroup());
            obGreen[i].clear();
        }
        for (int i = 0; i < obN; i++) {
            obBlue[i] = new SphereObject(0, 0, allRadius, "blue");
            root.getChildren().add(obBlue[i].getGroup());
            obBlue[i].clear();
        }

        //wave
        for (int i = 0; i < waN; i++) {
            waRed[i] = new Disc(0, 0, 3000, "red");
            root.getChildren().add(waRed[i].getGroupDisc());
            waRed[i].clear();
        }
        for (int i = 0; i < waN; i++) {
            waGreen[i] = new Disc(0, 0, 3000, "green");
            root.getChildren().add(waGreen[i].getGroupDisc());
            waGreen[i].clear();
        }
        for (int i = 0; i < waN; i++) {
            waBlue[i] = new Disc(0, 0, 3000, "blue");
            root.getChildren().add(waBlue[i].getGroupDisc());
            waBlue[i].clear();
        }

        // Ground
        Box floor = new Board().getBoard();
        root.getChildren().add(floor);

        camera = new PerspectiveCamera(true);
        camera.setFieldOfView(50.0);
        camera.setFarClip(1000);

        scene = new Scene(root, 800, 600, true);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.setTitle("JavaFX 3D");
        stage.show();
    }

    public void render(GameState gameState) {
        enR = 0; enG = 0; enB = 0;
        spR = 0; spG = 0; spB = 0;
        obR = 0; obG = 0; obB = 0;
        waR = 0; waG = 0; waB = 0;

        drawPlayer(gameState.player);

        for (Enemy enemy : gameState.enemies) {
            drawEnemy(enemy);
        }
        // 攻撃の描画
        playerSnowman.setAttack(gameState.attack.isActive());

        for (Bullet bullet : gameState.bullets) {
            drawBullet(bullet);
        }

        for (Wave wave : gameState.waves) {
            drawWave(wave);
        }
        for (Orbit orbit : gameState.orbits) {
            drawOrbit(orbit);
        }

        for (; enR < enN; enR++) {
            monsterRed[enR].clear();
        }
        for (; enG < enN; enG++) {
            monsterGreen[enG].clear();
        }
        for (; enB < enN; enB++) {
            monsterBlue[enB].clear();
        }

        for (int i = 0; i < enN; i++) {
            monsterRed[i].update();
            monsterGreen[i].update();
            monsterBlue[i].update();
        }

        for (; spR < spN; spR++) {
            spRed[spR].clear();
        }
        for (; spG < spN; spG++) {
            spGreen[spG].clear();
        }
        for (; spB < spN; spB++) {
            spBlue[spB].clear();
        }

        for (int i = 0; i < spN; i++) {
            spRed[i].update();
            spGreen[i].update();
            spBlue[i].update();
        }

        for (; obR < obN; obR++) {
            obRed[obR].clear();
        }
        for (; obG < obN; obG++) {
            obGreen[obG].clear();
        }
        for (; obB < obN; obB++) {
            obBlue[obB].clear();
        }

        for (int i = 0; i < obN; i++) {
            obRed[i].update();
            obGreen[i].update();
            obBlue[i].update();
        }

        // wave
        for (; waR < waN; waR++) {
            waRed[waR].clear();
        }
        for (; waG < waN; waG++) {
            waGreen[waG].clear();
        }
        for (; waB < waN; waB++) {
            waBlue[waB].clear();
        }

        for (int i = 0; i < waN; i++) {
            waRed[i].update();
            waGreen[i].update();
            waBlue[i].update();
        }

        camera.getTransforms().setAll(
                new Rotate(playerSnowman.getRotate(), playerSnowman.getX(), playerSnowman.getY(), playerSnowman.getZ(), Rotate.Z_AXIS),
                new Rotate(0, playerSnowman.getX(), playerSnowman.getY(), playerSnowman.getZ(), Rotate.Y_AXIS),
                new Rotate(60, playerSnowman.getX(), playerSnowman.getY(), playerSnowman.getZ(), Rotate.X_AXIS),
                new Translate(playerSnowman.getX(), playerSnowman.getY(), -100)
        );
    }
    private void drawPlayer(Player player) {
        playerSnowman.setX(player.getX());
        playerSnowman.setY(player.getY());
        playerSnowman.setRotate(Math.toDegrees(player.getAngle() + Math.PI /2));
        playerSnowman.setType(player.getType());
        playerSnowman.update();
    }

    private void drawEnemy(Enemy enemy) {
        switch (enemy.getType()) {
            case "red":
                if (enR < enN) {
                    monsterRed[enR].setX(enemy.getX());
                    monsterRed[enR].setY(enemy.getY());
                    monsterRed[enR].setRotate(Math.toDegrees(enemy.getAngle() + Math.PI / 2));
                    monsterRed[enR].update();
                    enR++;
                }
                break;
            case "green":
                if (enG < enN) {
                    monsterGreen[enG].setX(enemy.getX());
                    monsterGreen[enG].setY(enemy.getY());
                    monsterGreen[enG].setRotate(Math.toDegrees(enemy.getAngle() + Math.PI / 2));
                    monsterGreen[enG].update();
                    enG++;
                }
                break;
            case "blue":
                if (enB < enN) {
                    monsterBlue[enB].setX(enemy.getX());
                    monsterBlue[enB].setY(enemy.getY());
                    monsterBlue[enB].setRotate(Math.toDegrees(enemy.getAngle() + Math.PI / 2));
                    monsterBlue[enB].update();
                    enB++;
                }
                break;
        }
    }


    private void drawBullet(Bullet bullet) {
        switch (bullet.getType()) {
            case "red":
                if (spR < spN) {
                    spRed[spR].setX(bullet.getX());
                    spRed[spR].setY(bullet.getY());
                    spRed[spR].update();
                    spR++;
                }
                break;
            case "green":
                if (spG < spN) {
                    spGreen[spG].setX(bullet.getX());
                    spGreen[spG].setY(bullet.getY());
                    spGreen[spG].update();
                    spG++;
                }
                break;
            case "blue":
                if (spB < spN) {
                    spBlue[spB].setX(bullet.getX());
                    spBlue[spB].setY(bullet.getY());
                    spBlue[spB].update();
                    spB++;
                }
                break;
        }
    }

    private void drawOrbit(Orbit orbit) {
        switch (orbit.getType()) {
            case "red":
                if (obR < obN) {
                    obRed[obR].setX(orbit.getX());
                    obRed[obR].setY(orbit.getY());
                    obRed[obR].update();
                    obR++;
                }
                break;
            case "green":
                if (obG < obN) {
                    obGreen[obG].setX(orbit.getX());
                    obGreen[obG].setY(orbit.getY());
                    obGreen[obG].update();
                    obG++;
                }
                break;
            case "blue":
                if (obB < obN) {
                    obBlue[obB].setX(orbit.getX());
                    obBlue[obB].setY(orbit.getY());
                    obBlue[obB].update();
                    obB++;
                }
                break;
        }
    }

    private void drawWave(Wave wave) {
        switch (wave.getType()) {
            case "red":
                if (waR < waN) {
                    waRed[waR].setX(wave.getX());
                    waRed[waR].setY(wave.getY());
                    waRed[waR].setDiscScale(wave.getRadius());
                    waRed[waR].update();
                    waR++;
                }
                break;
            case "green":
                if (waG < waN) {
                    waGreen[waG].setX(wave.getX());
                    waGreen[waG].setY(wave.getY());
                    waGreen[waG].setDiscScale(wave.getRadius());
                    waGreen[waG].update();
                    waG++;
                }
                break;
            case "blue":
                if (waB < waN) {
                    waBlue[waB].setX(wave.getX());
                    waBlue[waB].setY(wave.getY());
                    waBlue[waB].setDiscScale(wave.getRadius());
                    waBlue[waB].update();
                    waB++;
                }
                break;
        }
    }

    private Color getColor(String type) {
        switch (type) {
            case "red":
                return Color.RED;
            case "green":
                return Color.LIMEGREEN;
            case "blue":
                return Color.BLUE;
            default:
                return Color.WHITE;
        }
    }

    public Group getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }
}