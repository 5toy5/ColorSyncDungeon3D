package MVC.game.model;

public class Player {
    private double x, y, angle, speed;
    private String type;
    private int hp, score;
    private long lastDamagedTime;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = 1;
        this.speed = 3;
        this.hp = 100;
        this.score = 0;
        this.type = "red";
        this.lastDamagedTime = System.currentTimeMillis();
    }

    public void move(double da) {
        x += Math.cos(angle + da) * speed;
        y += Math.sin(angle + da) * speed;
    }

    public void rotate(double da) {
        angle += da;
    }

    public void changeType(String newType) {
        type = newType;
    }

    public void attack(Attack attack) {
        attack.newAttack(x, y, angle, type);
    }

    public void damaged() {
        if (System.currentTimeMillis() - lastDamagedTime > 200) {
            hp -= 1;
            lastDamagedTime = System.currentTimeMillis();
        }
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
    public String getType(){
        return this.type;
    }

    public int getHp() {
        return hp;
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

    public void setHp(int hp) {
        this.hp = hp;
    }
}