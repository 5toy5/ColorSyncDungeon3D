package toyoura.game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Engine_test extends Application {

    // Game global variables
    Player player;
    ArrayList<Enemy> enemies;
    Attack attack;
    ArrayList<Bullet> bullets;
    ArrayList<Orbit> orbits;
    ArrayList<Wave> waves;
    boolean gameOver;
    HashMap<KeyCode, Boolean> keys = new HashMap<>();
    int bgColor;

    // Canvas dimensions
    final int width = 800;
    final int height = 600;

    // Timing
    long lastNanoTime;

    @Override
    public void start(Stage primaryStage) {
        player = new Player(width / 2, height / 2);
        attack = new Attack(player.x, player.y, player.angle, player.type);
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        orbits = new ArrayList<>();
        waves = new ArrayList<>();
        gameOver = false;

        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().addAll(canvas);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Game Recreation");
        primaryStage.show();

        // Handle key presses
        scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
        scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));

        lastNanoTime = System.nanoTime();

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1_000_000.0;
                lastNanoTime = currentNanoTime;

                // Update game logic
                handlePlayerInput();
                updateGame(elapsedTime);

                // Render
                render(gc);
            }
        }.start();
    }

    void handlePlayerInput() {
        if (isKeyPressed(KeyCode.W)) player.move(0);
        if (isKeyPressed(KeyCode.S)) player.move(Math.PI);
        if (isKeyPressed(KeyCode.A)) player.move(-Math.PI / 2);
        if (isKeyPressed(KeyCode.D)) player.move(Math.PI / 2);

        if (isKeyPressed(KeyCode.Q)) player.rotate(-0.1);
        if (isKeyPressed(KeyCode.E)) player.rotate(0.1);

        if (isKeyPressed(KeyCode.R)) player.changeType("red");
        if (isKeyPressed(KeyCode.G)) player.changeType("green");
        if (isKeyPressed(KeyCode.B)) player.changeType("blue");

        if (isKeyPressed(KeyCode.SPACE)) player.attack(attack);
    }

    void updateGame(double elapsedTime) {
        if (gameOver) return;

        // Update enemies
        for (Enemy enemy : enemies) {
            enemy.move();
            enemy.attack();
        }

        // Update attack
        if (attack.isActive()) {
            for (Enemy enemy : enemies) {
                if (attack.isHit(enemy)) {
                    enemy.respawn();
                }
            }
        }

        // Update bullets
        Iterator<Bullet> bulletIter = bullets.iterator();
        while (bulletIter.hasNext()) {
            Bullet bullet = bulletIter.next();
            bullet.move();
            if (bullet.isOutOfBounds(width, height)) {
                bulletIter.remove();
            } else if (bullet.isHit(player)) {
                bulletIter.remove();
                player.damaged();
            }
        }

        // Update orbits
        Iterator<Orbit> orbitIter = orbits.iterator();
        while (orbitIter.hasNext()) {
            Orbit orbit = orbitIter.next();
            if (orbit.isActive()) {
                orbit.move();
                if (orbit.isOutOfBounds(width, height)) {
                    orbitIter.remove();
                } else if (orbit.isHit(player)) {
                    orbitIter.remove();
                    player.damaged();
                }
            } else {
                orbitIter.remove();
            }
        }

        // Update waves
        Iterator<Wave> waveIter = waves.iterator();
        while (waveIter.hasNext()) {
            Wave wave = waveIter.next();
            if (wave.isActive()) {
                wave.update();
                if (wave.isHit(player)) {
                    player.damaged();
                }
            } else {
                waveIter.remove();
            }
        }

        // Check collisions with enemies
        for (Enemy enemy : enemies) {
            if (enemy.isCollidingWith(player)) {
                enemy.respawn();
                player.damaged();
            }
        }
    }

    void render(GraphicsContext gc) {
        // Clear canvas
        gc.setFill(Color.rgb(bgColor, bgColor, bgColor));
        gc.fillRect(0, 0, width, height);

        if (gameOver) {
            gc.setFill(Color.WHITE);
            gc.fillText("Game Over", width / 2 - 50, height / 2);
            return;
        }

        // Render player
        player.display(gc);

        // Render enemies
        for (Enemy enemy : enemies) {
            enemy.display(gc);
        }

        // Render attack
        if (attack.isActive()) {
            attack.display(gc);
        }

        // Render bullets
        for (Bullet bullet : bullets) {
            bullet.display(gc);
        }

        // Render orbits
        for (Orbit orbit : orbits) {
            orbit.display(gc);
        }

        // Render waves
        for (Wave wave : waves) {
            wave.display(gc);
        }
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

    boolean isEffective(String attackType, String targetType) {
        return (attackType.equals("red") && targetType.equals("green")) ||
                (attackType.equals("green") && targetType.equals("blue")) ||
                (attackType.equals("blue") && targetType.equals("red"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Player class
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
            x = clamp(x, 0, width);
            y = clamp(y, 0, height);
        }

        void rotate(double da) {
            angle += da;
        }

        void changeType(String newType) {
            type = newType;
        }

        void attack(Attack a) {
            a.newAttack(x, y, angle, type);
        }

        void damaged() {
            if (System.currentTimeMillis() - lastDamagedTime > 200) {
                hp -= 1;
                if (hp <= 0) gameOver = true;
                lastDamagedTime = System.currentTimeMillis();
            }
        }

        void display(GraphicsContext gc) {
            bgColor = 25 * hp;

            gc.setFill(getColor(type));
            gc.fillOval(x - 15, y - 15, 30, 30);

            gc.setFill(Color.WHITE);
            gc.fillOval(
                    x + Math.cos(angle) * 10 - 5,
                    y + Math.sin(angle) * 10 - 5,
                    10, 10
            );
        }
    }

    // Enemy class
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
            x = Math.random() * width;
            y = Math.random() * height;
            angle = Math.random() * (2 * Math.PI) - Math.PI;
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

        void attack() {
            long currentTime = System.currentTimeMillis();

            // Bullet attack
            if (currentTime - lastBulletTime > nextBulletTime) {
                angle = Math.atan2(player.y - y, player.x - x);
                da = Math.random() * (Math.PI / 25) - (Math.PI / 50);
                bullets.add(new Bullet(x, y, angle, type));
                lastBulletTime = currentTime;
                nextBulletTime = (long) (Math.random() * 2000) + 1000;
            }

            // Orbit attack
            if (currentTime - lastOrbitTime > nextOrbitTime) {
                for (int i = 0; i < 10; i++) {
                    orbits.add(new Orbit(x, y, Math.atan2(player.y - y, player.x - x) + i * 0.1, type));
                }
                lastOrbitTime = currentTime;
                nextOrbitTime = (long) (Math.random() * 2000) + 3000;
            }

            // Wave attack
            if (currentTime - lastWaveTime > nextWaveTime) {
                waves.add(new Wave(x, y, type));
                lastWaveTime = currentTime;
                nextWaveTime = (long) (Math.random() * 5000) + 5000;
            }
        }

        void display(GraphicsContext gc) {
            gc.setFill(getColor(type));
            gc.fillOval(x - 10, y - 10, 20, 20);

            gc.setFill(Color.BLACK);
            gc.fillOval(
                    x + Math.cos(angle) * 10 - 5,
                    y + Math.sin(angle) * 10 - 5,
                    10, 10
            );
        }

        String randomType() {
            int r = (int) (Math.random() * 3);
            if (r == 0) return "red";
            if (r == 1) return "green";
            return "blue";
        }

        boolean isCollidingWith(Player p) {
            return distance(x, y, p.x, p.y) < 20 && isEffective(type, p.type);
        }
    }

    // Attack class
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
            double angleToEnemy = angleBetween(player.x, player.y, e.x, e.y);
            double angleDiff = fixAngle(player.angle - angleToEnemy);
            double dist = distance(player.x, player.y, e.x, e.y);
            return Math.abs(angleDiff) < angleRange &&
                    dist < range &&
                    isEffective(player.type, e.type);
        }

        void display(GraphicsContext gc) {
            gc.setFill(getColor(type));
            gc.fillArc(
                    player.x - range,
                    player.y - range,
                    range * 2,
                    range * 2,
                    Math.toDegrees(-player.angle - angleRange),
                    Math.toDegrees(angleRange * 2),
                    javafx.scene.shape.ArcType.ROUND
            );
        }
    }

    // Bullet class
    class Bullet {
        double x, y, angle, speed, radius;
        String type;

        Bullet(double x, double y, double angle, String type) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.type = type;
            this.speed = 3;
            this.radius = 10;
        }

        void move() {
            x += Math.cos(angle) * speed;
            y += Math.sin(angle) * speed;
        }

        boolean isHit(Player p) {
            return distance(x, y, p.x, p.y) < radius && isEffective(type, p.type);
        }

        boolean isOutOfBounds(double w, double h) {
            return x < 0 || x > w || y < 0 || y > h;
        }

        void display(GraphicsContext gc) {
            gc.setFill(getColor(type));
            gc.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        }
    }

    // Wave class
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

        void display(GraphicsContext gc) {
            gc.setStroke(getColor(type));
            gc.setLineWidth(2);
            gc.strokeOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    // Orbit class
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

        void display(GraphicsContext gc) {
            gc.setFill(getColor(type));
            gc.fillOval(x - radius / 2, y - radius / 2, radius, radius);
        }
    }

    // Utility methods
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