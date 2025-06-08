package MVC.game.model3D;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Translate;

import java.util.Objects;

public class SphereObject {
    double x, y, radius;
    String str;
    Sphere sphere;  // Sphereを直接保持する
    Group group;

    public SphereObject(double x, double y, double radius, String str) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.str = str;

        sphere = createSphere();
        group = new Group(sphere);
    }

    private Sphere createSphere() {
        Sphere sphere = new Sphere(radius);
        if (Objects.equals(str, "red")) {
            sphere.setMaterial(new PhongMaterial(Color.RED));
        } else if (Objects.equals(str, "green")) {
            sphere.setMaterial(new PhongMaterial(Color.GREEN));
        } else if (Objects.equals(str, "blue")) {
            sphere.setMaterial(new PhongMaterial(Color.BLUE));
        }
        sphere.getTransforms().add(new Translate(x, y, 0));
        return sphere;
    }

    public Group getGroup() {
        return group;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void clear() {
        this.x = 10000; this.y = 10000;
    }

//    public Node getSphere() {
//        return sphere;
//    }
    public void update() {
        this.sphere.getTransforms().setAll(new Translate(x, y, 0));
    }
}
