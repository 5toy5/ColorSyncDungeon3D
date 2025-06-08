package MVC.game.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import MVC.game.model.*;

import java.util.List;

public class GameView {
    private GraphicsContext gc;

    public GameView(GraphicsContext gc) {
        this.gc = gc;
    }

    public void render(GameState gameState) {
        // 背景のクリア
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, 800, 600);

        // プレイヤーの描画
        drawPlayer(gameState.player);

        // 敵の描画
        for (Enemy enemy : gameState.enemies) {
            drawEnemy(enemy);
        }

        // 攻撃の描画
        if (gameState.attack.isActive()) {
            drawAttack(gameState.attack);
        }

        // 銃弾の描画
        for (Bullet bullet : gameState.bullets) {
            drawBullet(bullet);
        }

        // 衝撃波の描画
        for (Wave wave : gameState.waves) {
            drawWave(wave);
        }

        // 軌道弾の描画
        for (Orbit orbit : gameState.orbits) {
            drawOrbit(orbit);
        }
    }
    private void drawPlayer(Player player) {
        gc.setFill(getColor(player.getType()));
        gc.fillOval(player.getX() - 15, player.getY() - 15, 30, 30);

        gc.setFill(Color.WHITE);
        gc.fillOval(
                player.getX() + Math.cos(player.getAngle()) * 10 - 5,
                player.getY() + Math.sin(player.getAngle()) * 10 - 5,
                10, 10
        );
    }

    private void drawEnemy(Enemy enemy) {
        gc.setFill(getColor(enemy.getType()));
        gc.fillOval(enemy.getX() - 10, enemy.getY() - 10, 20, 20);

        gc.setFill(Color.BLACK);
        gc.fillOval(
                enemy.getX() + Math.cos(enemy.getAngle()) * 10 - 5,
                enemy.getY() + Math.sin(enemy.getAngle()) * 10 - 5,
                10, 10
        );
    }

    private void drawAttack(Attack attack) {
        gc.setFill(getColor(attack.getType()));
        gc.fillArc(
                attack.getX() - attack.getRange(),
                attack.getY() - attack.getRange(),
                attack.getRange() * 2,
                attack.getRange() * 2,
                Math.toDegrees(-attack.getAngle() - attack.getAngleRange()),
                Math.toDegrees(attack.getAngleRange() * 2),
                javafx.scene.shape.ArcType.ROUND
        );
    }

    private void drawBullet(Bullet bullet) {
        gc.setFill(getColor(bullet.getType()));
        gc.fillOval(bullet.getX() - bullet.getRadius() / 2, bullet.getY() - bullet.getRadius() / 2, bullet.getRadius(), bullet.getRadius());
    }

    private void drawWave(Wave wave) {
        gc.setStroke(getColor(wave.getType()));
        gc.setLineWidth(2);
        gc.strokeOval(wave.getX() - wave.getRadius(), wave.getY() - wave.getRadius(), wave.getRadius() * 2, wave.getRadius() * 2);
    }

    private void drawOrbit(Orbit orbit) {
        gc.setFill(getColor(orbit.getType()));
        gc.fillOval(orbit.getX() - orbit.getRadius() / 2, orbit.getY() - orbit.getRadius() / 2, orbit.getRadius(), orbit.getRadius());
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
}
