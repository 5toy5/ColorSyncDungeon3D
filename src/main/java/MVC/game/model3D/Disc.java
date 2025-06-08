package MVC.game.model3D;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.util.Objects;

public class Disc {
    double x, y, z, radius;
    String str;
    Group group;

    public Disc(double x, double y, double radius, String str) {
        this.x = x;
        this.y = y;
        this.z = -2;
        this.radius = radius;
        this.str = str;
        createDisc();
    }

    public void createDisc() {
        double discRadius = radius;
        double discHeight = 1;
        Cylinder disc = new Cylinder(discRadius, discHeight);
        if (Objects.equals(str, "red")) disc.setMaterial(new PhongMaterial(Color.rgb(255,0,0)));
        else if (Objects.equals(str, "green")) disc.setMaterial(new PhongMaterial(Color.rgb(0,255,0)));
        else if (Objects.equals(str, "blue")) disc.setMaterial(new PhongMaterial(Color.rgb(0,0,255)));
//        else disc.setMaterial(new PhongMaterial(Color.BLUE));
        double centerX = x;
        double centerY = y;
        double centerZ = z;
        disc.getTransforms().addAll(
                new Translate(centerX, centerY, centerZ),
                new Rotate(90, Rotate.X_AXIS)
        );

        group = new Group(disc);
    }

//    public void clearDisc() {
//        group.getChildren().clear();
//    }

    public void clear(){
        this.x = 10000; this.y = 10000;
    }

    public void setDiscScale(double newRadius) {
        double scaleFactor = newRadius / this.radius;
        for (Node node : group.getChildren()) {
            if (node instanceof Cylinder) {
                node.getTransforms().add(new Scale(scaleFactor, 1, scaleFactor));
            }
        }
        this.radius = newRadius; // Update the current radius to the new radius
    }

    public void setX(double newX){
        this.x = newX;
    }

    public void setY(double newY){
        this.y = newY;
    }

    public void setType(String newStr){
        this.str = newStr;
    }

    public void update() {
        this.group.getTransforms().setAll(
//                new Rotate(90, Rotate.X_AXIS),
                new Translate(x, y , z)
        );
    }

    public Group getGroupDisc() {
        return group;
    }
}
