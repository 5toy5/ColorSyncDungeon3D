package MVC.game.model;

// Bulletクラス
public class Bullet {
    double x, y, angle, speed, radius;
    String type;

    public Bullet(double x, double y, double angle, String type) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.type = type;
        this.speed = 3;
        this.radius = 10;
    }

    public void move() {
        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
    }

    public boolean isHit(Player p) {
        return distance(x, y, p.getX(), p.getY()) < radius && isEffective(type, p.getType());
    }

    public boolean isOutOfBounds(double w, double h) {
        return x < 400- w/2 || x > 400+ w/2 || y < 300- h/2 || y > 300+ h/2;
    }

    double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    boolean isEffective(String attackType, String targetType) {
        return (attackType.equals("red") && targetType.equals("green")) ||
                (attackType.equals("green") && targetType.equals("blue")) ||
                (attackType.equals("blue") && targetType.equals("red"));
    }

    // ゲッターとセッター
    public double getX(){
        return this.x;
    }
    public double getY(){
        return this.y;
    }
    public String getType() {
        return type;
    }
    public double getRadius() {
        return radius;
    }
}
