package MVC.game.model;

public class Wave {
    double x, y, radius;
    String type;
    long startTime;

    public Wave(double x, double y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.radius = 0;
        this.startTime = System.currentTimeMillis();
    }

    public void update() {
        radius += 2;
    }

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < 1000;
    }

    public boolean isHit(Player p) {
        return distance(x, y, p.getX(), p.getY()) < radius && isEffective(type, p.getType());
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
