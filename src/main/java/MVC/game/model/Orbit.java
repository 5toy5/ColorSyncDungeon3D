package MVC.game.model;

// Orbitクラス
public class Orbit {
    double x, y, radius, orbitR, orbitV, angle, angleV, anchorX, anchorY;
    String type;
    long startTime;

    public Orbit(double x, double y, double angle, String type) {
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

    public void move() {
        angle += angleV;
        orbitR += orbitV;
        x = Math.cos(angle) * orbitR + anchorX;
        y = Math.sin(angle) * orbitR + anchorY;
    }

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < 7000;
    }

    public boolean isHit(Player p) {
        return distance(x, y, p.getX(), p.getY()) < radius && isEffective(type, p.getType());
    }

    public boolean isOutOfBounds(double w, double h) {
        return x < 400- w/2 || x > 400+ w/2 || y < 300- h/2 || y > 300+ h/2;
    }

    double fixAngle(double angle) {
        while (angle <= -Math.PI) angle += 2 * Math.PI;
        while (angle > Math.PI) angle -= 2 * Math.PI;
        return angle;
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
