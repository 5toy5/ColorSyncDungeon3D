package MVC.game.model;

// Attackクラス
public class Attack {
    double x, y, angle, range, angleRange;
    String type;
    long startTime;

    public Attack(double x, double y, double angle, String type) {
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

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < 500;
    }

    public boolean isHit(Player p, Enemy e) {
        double angleToEnemy = angleBetween(p.getX(), p.getY(), e.getX(), e.getY());
        double angleDiff = fixAngle(p.getAngle() - angleToEnemy);
        double dist = distance(p.getX(), p.getY(), e.getX(), e.getY());
        return Math.abs(angleDiff) < angleRange &&
                dist < range &&
                isEffective(p.getType(), e.getType());
    }

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
    public double getAngle() {
        return angle;
    }
    public double getRange() {
        return range;
    }
    public double getAngleRange() {
        return angleRange;
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }
    public void setAngle(double angle) {
        this.angle = angle;
    }
    public void setRange(double range) {
        this.range = range;
    }
    public void setAngleRange(double angleRange) {
        this.angleRange = angleRange;
    }
    public void setType(String type) {
        this.type = type;
    }
}
