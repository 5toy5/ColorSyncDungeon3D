package MVC.game.model;

import MVC.game.view.GameView;
import MVC.game.view.GameView3D;

import javafx.scene.input.KeyCode;
import java.util.*;

public class GameState {
    public Player player;
    public List<Enemy> enemies;
    public Attack attack;
    public List<Bullet> bullets;
    public List<Wave> waves;
    public List<Orbit> orbits;
    public boolean gameOver;
    public Map<KeyCode, Boolean> keys;
    private GameView gameView;
    private GameView3D gameView3D;
    private boolean use3DView;

    public GameState(GameView gameView, GameView3D gameView3D, boolean use3DView) {
        this.player = new Player(400, 300);
        this.enemies = new ArrayList<>();
        this.attack = new Attack(player.getX(), player.getY(), player.getAngle(), player.getType());
        this.bullets = new ArrayList<>();
        this.waves = new ArrayList<>();
        this.orbits = new ArrayList<>();
        this.keys = new HashMap<>();
        this.gameOver = false;
        this.gameView = gameView;
        this.gameView3D = gameView3D;
        this.use3DView = use3DView;

        initEnemies();
    }

    private void initEnemies() {
        for (int i = 0; i < 5; i++) {
            enemies.add(new Enemy());
        }
    }

    public void update() {
        // Update player
        // ...

        // Update enemies and their attacks
        for (Enemy enemy : enemies) {
            enemy.move();
            enemy.attack(player, attack,this);
        }

        // Update bullets, attacks, waves, and orbits
        updateAttack();
        updateBullets();
        updateWaves();
        updateOrbits();

        // Check collisions
        checkCollisions();
    }

    public void render() {
        if (use3DView) {
            gameView3D.render(this);
        } else {
            gameView.render(this);
        }
    }

    private void checkCollisions() {
        // Check if attack hits enemies
        if (attack.isActive()) {
            for (Enemy enemy : enemies) {
                if (attack.isHit(player, enemy)) {
                    enemy.respawn();
                }
            }
        }

        // Check if bullets hit player
        bullets.removeIf(bullet -> {
            if (bullet.isHit(player)) {
                player.damaged();
                return true;
            }
            return false;
        });

        // Check if waves hit player
        for (Wave wave : waves) {
            if (wave.isHit(player)) {
                player.damaged();
            }
        }

        // Check if orbits hit player
        orbits.removeIf(orbit -> {
            if (orbit.isHit(player)) {
                player.damaged();
                return true;
            }
            return false;
        });

        // Check if player collides with enemies
        for (Enemy enemy : enemies) {
            if (enemy.isCollidingWithPlayer(player)) {
                enemy.respawn();
                player.damaged();
            }
        }

        if (player.getHp() <= 0) {
            gameOver = true;
        }
    }

    private void updateAttack() {
        attack.setX(player.getX());
        attack.setY(player.getY());
        attack.setAngle(player.getAngle());
    }

    private void updateBullets() {
        bullets.forEach(Bullet::move);
        bullets.removeIf(bullet -> bullet.isOutOfBounds(800, 600) && distance(bullet.getX(), bullet.getY(), player.getX(), player.getY()) > 1000);
    }

    private void updateWaves() {
        waves.forEach(Wave::update);
        waves.removeIf(wave -> !wave.isActive());
    }

    private void updateOrbits() {
        orbits.forEach(Orbit::move);
        orbits.removeIf(orbit -> !orbit.isActive() || orbit.isOutOfBounds(800, 600) && distance(orbit.getX(), orbit.getY(), player.getX(), player.getY()) > 1000);
    }

    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public void addWave(Wave wave) {
        waves.add(wave);
    }

    public void addOrbit(Orbit orbit) {
        orbits.add(orbit);
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }
}