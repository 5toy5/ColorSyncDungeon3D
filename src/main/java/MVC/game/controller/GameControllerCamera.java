//package MVC.game.controller;
//
//import javafx.animation.AnimationTimer;
//import javafx.scene.Scene;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.input.KeyCode;
//import javafx.stage.Stage;
//
//import MVC.game.model.*;
//import MVC.game.view.GameView;
//import MVC.game.view.GameView3D;
//
//import java.util.*;
//
//public class GameControllerCamera {
//    private Player player;
//    private List<Enemy> enemies;
//    private Attack attack;
//    private List<Bullet> bullets;
//    private List<Wave> waves;
//    private List<Orbit> orbits;
//
//    private GameView gameView;
//    private GameView3D gameView3D;
//    private Map<KeyCode, Boolean> keys;
//    private boolean gameOver;
//    private boolean use3DView;
//
//    public GameControllerCamera(GraphicsContext gc, Scene scene, Stage stage, boolean use3DView) throws Exception {
//        this.use3DView = use3DView;
//        this.player = new Player(400, 300);
//        this.enemies = new ArrayList<>();
//        this.attack = new Attack(player.getX(), player.getY(), player.getAngle(), player.getType());
//        this.bullets = new ArrayList<>();
//        this.waves = new ArrayList<>();
//        this.orbits = new ArrayList<>();
//
//        this.keys = new HashMap<>();
//        this.gameOver = false;
//
//        if (use3DView) {
//            this.gameView3D = new GameView3D(stage);
//            scene = gameView3D.getScene();
//        } else {
//            this.gameView = new GameView(gc);
//        }
//
//        initEnemies();
//
//        // ユーザー入力の設定
//        scene.setOnKeyPressed(e -> keys.put(e.getCode(), true));
//        scene.setOnKeyReleased(e -> keys.put(e.getCode(), false));
//
//        startGameLoop();
//    }
//
//    private void initEnemies() {
//        for (int i = 0; i < 5; i++) {
//            enemies.add(new Enemy());
//        }
//    }
//
//    private void startGameLoop() {
//        new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (!gameOver) {
//                    update();
//                    render();
//                } else {
//                    // ゲームオーバーの処理
//                }
//            }
//        }.start();
//    }
//
//    private void update() {
//        handleInput();
//
//        // プレイヤーの更新
//        // ...
//
//        // 敵の更新と攻撃
//        for (Enemy enemy : enemies) {
//            enemy.move();
////            enemy.attack(player, attack, this);
//        }
//
//        // 弾や攻撃の更新
//        updateAttack();
//        updateBullets();
//        updateWaves();
//        updateOrbits();
//
//        // 衝突判定
//        checkCollisions();
//    }
//
//    private void render() {
//        if (use3DView) {
//            gameView3D.render(player, enemies, attack, bullets, waves, orbits);
//        } else {
//            gameView.render(player, enemies, attack, bullets, waves, orbits);
//        }
//    }
//
//    private void handleInput() {
//        if (isPressed(KeyCode.W)) player.move(0);
//        if (isPressed(KeyCode.S)) player.move(Math.PI);
//        if (isPressed(KeyCode.A)) player.move(-Math.PI / 2);
//        if (isPressed(KeyCode.D)) player.move(Math.PI / 2);
//
//        if (isPressed(KeyCode.Q)) player.rotate(-0.1);
//        if (isPressed(KeyCode.E)) player.rotate(0.1);
//
//        if (isPressed(KeyCode.R)) player.changeType("red");
//        if (isPressed(KeyCode.G)) player.changeType("green");
//        if (isPressed(KeyCode.B)) player.changeType("blue");
//
//        if (isPressed(KeyCode.SPACE)) player.attack(attack);
//    }
//
//    private void checkCollisions() {
//        // 攻撃が敵に当たったか確認
//        if (attack.isActive()) {
//            for (Enemy enemy : enemies) {
//                if (attack.isHit(player, enemy)) {
//                    enemy.respawn();
//                }
//            }
//        }
//
//        // 弾がプレイヤーに当たったか確認
//        bullets.removeIf(bullet -> {
//            if (bullet.isHit(player)) {
//                player.damaged();
//                return true;
//            }
//            return false;
//        });
//
//        // 波がプレイヤーに当たったか確認
//        for (Wave wave : waves) {
//            if (wave.isHit(player)) {
//                player.damaged();
//            }
//        }
//
//        // 軌道弾がプレイヤーに当たったか確認
//        orbits.removeIf(orbit -> {
//            if (orbit.isHit(player)) {
//                player.damaged();
//                return true;
//            }
//            return false;
//        });
//
//        // プレイヤーが敵に接触したか確認
//        for (Enemy enemy : enemies) {
//            if (enemy.isCollidingWithPlayer(player)) {
//                enemy.respawn();
//                player.damaged();
//            }
//        }
//
//        if (player.getHp() <= 0) {
//            gameOver = true;
//        }
//    }
//
//    private void updateAttack() {
//        attack.setX(player.getX());
//        attack.setY(player.getY());
//        attack.setAngle(player.getAngle());
//    }
//
//    private void updateBullets() {
//        bullets.forEach(Bullet::move);
//        bullets.removeIf(bullet -> bullet.isOutOfBounds(800, 600) && distance(bullet.getX(), bullet.getY(), player.getX(), player.getY())>1000);
//    }
//
//    private void updateWaves() {
//        waves.forEach(Wave::update);
//        waves.removeIf(wave -> !wave.isActive());
//    }
//
//    private void updateOrbits() {
//        orbits.forEach(Orbit::move);
//        orbits.removeIf(orbit -> !orbit.isActive() || orbit.isOutOfBounds(800, 600) && distance(orbit.getX(), orbit.getY(), player.getX(), player.getY())>1000);
//    }
//
//    private boolean isPressed(KeyCode key) {
//        return keys.getOrDefault(key, false);
//    }
//
//    // 弾や波、軌道弾をコントローラーに追加するメソッド
//    public void addBullet(Bullet bullet) {
//        bullets.add(bullet);
//    }
//
//    public void addWave(Wave wave) {
//        waves.add(wave);
//    }
//
//    public void addOrbit(Orbit orbit) {
//        orbits.add(orbit);
//    }
//
//    double distance(double x1, double y1, double x2, double y2) {
//        return Math.hypot(x2 - x1, y2 - y1);
//    }
//}