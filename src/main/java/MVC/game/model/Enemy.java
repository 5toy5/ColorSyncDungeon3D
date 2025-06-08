package MVC.game.model;

import MVC.game.controller.GameController;

import java.util.Random;

public class Enemy {
    private double x, y, angle, speed, da;
    private String type;
    private long lastBulletTime, nextBulletTime;
    private long lastOrbitTime, nextOrbitTime;
    private long lastWaveTime, nextWaveTime;

    public Enemy() {
        respawn();
        long currentTime = System.currentTimeMillis();
        lastBulletTime = currentTime;
        lastOrbitTime = currentTime;
        lastWaveTime = currentTime;
    }

    public void respawn() {
        Random rand = new Random();
        x = rand.nextDouble() * 800;
        y = rand.nextDouble() * 600;
        angle = rand.nextDouble() * (2 * Math.PI) - Math.PI;
        speed = 1;
        type = randomType();
    }

    public void move() {
        angle += da;
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
    }

    public void attack(Player player, Attack attack, GameState gameState) {
        long currentTime = System.currentTimeMillis();
        Random rand = new Random();

        // 銃弾攻撃
        if (currentTime - lastBulletTime > nextBulletTime) {
            angle = Math.atan2(player.getY() - y, player.getX() - x);
            da = rand.nextDouble() * (Math.PI / 25) - (Math.PI / 50);
            gameState.addBullet(new Bullet(x, y, angle, type));
            lastBulletTime = currentTime;
            nextBulletTime = rand.nextInt(2000) + 1000;
        }

        // 軌道弾攻撃
        if (currentTime - lastOrbitTime > nextOrbitTime) {
            for (int i = 0; i < 10; i++) {
                gameState.addOrbit(new Orbit(x, y, angle + i * 0.1, type));
            }
            lastOrbitTime = currentTime;
            nextOrbitTime = rand.nextInt(2000) + 3000;
        }

        // 衝撃波攻撃
        if (currentTime - lastWaveTime > nextWaveTime) {
            gameState.addWave(new Wave(x, y, type));
            lastWaveTime = currentTime;
            nextWaveTime = rand.nextInt(5000) + 5000;
        }
    }

    private String randomType() {
        Random rand = new Random();
        int r = rand.nextInt(3);
        if (r == 0) return "red";
        if (r == 1) return "green";
        return "blue";
    }

    public boolean isCollidingWithPlayer(Player p) {
        return distance(x, y, p.getX(), p.getY()) < 20 && isEffective(type, p.getType());
    }

    // ゲッターとセッター
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public double getAngle(){
        return this.angle;
    }
    public double getSpeed() {
        return this.speed;
    }
    public double getDa() {
        return this.da;
    }
    public String getType(){
        return this.type;
    }

    public void setX(double newX){
        this.x = newX;
    }
    public void setY(double newY){
        this.y = newY;
    }
    public void setAngle(double newAngle){
        this.angle = newAngle;
    }
    public void setSpeed(double newSpeed){
        this.speed = newSpeed;
    }
    public void set(String newType){
        this.type = newType;
    }

    double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    boolean isEffective(String attackType, String targetType) {
        return (attackType.equals("red") && targetType.equals("green")) ||
                (attackType.equals("green") && targetType.equals("blue")) ||
                (attackType.equals("blue") && targetType.equals("red"));
    }
}
