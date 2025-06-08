package MVC.game.model3D;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Model3D;
import org.fxyz3d.importers.obj.ObjImporter;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Snowman {
    double x, y, zRed, zGreen, zBlue, rotate;
    double redAttackZ, greenAttackZ, blueAttackZ;
    double showZ = 0;
    double clearZ = 50;
    double baseRadius, topRadius, offsetY;
    double attackSize = 1.0; //攻撃の扇形の半径=100*attackSizeで計算;
    boolean isAttack;
    String str;
    Group groupFace;
    Group groupRed;
    Group groupGreen;
    Group groupBlue;
    Group groupRedAttack, groupGreenAttack, groupBlueAttack;
    File fileRed, fileGreen, fileBlue;

    public Snowman(double x, double y, double rotate, double baseRadius, double topRadius, double offsetY, String str, boolean isAttack) throws Exception {
        this.x = x;
        this.y = y;
        this.rotate = rotate;
        this.baseRadius = baseRadius;
        this.topRadius = topRadius;
        this.offsetY = offsetY;
        this.str = str;
        this.isAttack = isAttack;

        // グループの初期化
        this.groupRedAttack = new Group();
        this.groupGreenAttack = new Group();
        this.groupBlueAttack = new Group();

        drawSnowman();
    }

    public void drawSnowman() throws Exception {
        Group newGroupFace;
        Group newGroupRed;
        Group newGroupGreen;
        Group newGroupBlue;
        Sphere baseRed = new Sphere(baseRadius); Sphere baseGreen = new Sphere(baseRadius); Sphere baseBlue = new Sphere(baseRadius);
        Sphere topRed = new Sphere(topRadius); Sphere topGreen = new Sphere(topRadius); Sphere topBlue = new Sphere(topRadius);
        baseRed.setMaterial(new PhongMaterial(Color.RED));
        topRed.setMaterial(new PhongMaterial(Color.RED));
        baseGreen.setMaterial(new PhongMaterial(Color.GREEN));
        topGreen.setMaterial(new PhongMaterial(Color.GREEN));
        baseBlue.setMaterial(new PhongMaterial(Color.BLUE));
        topBlue.setMaterial(new PhongMaterial(Color.BLUE));

        baseRed.getTransforms().add(new Translate(0,0,zRed));
        baseGreen.getTransforms().add(new Translate(0,0,zGreen));
        baseBlue.getTransforms().add(new Translate(0,0,zBlue));
        topRed.getTransforms().add(new Translate(0, 0, zRed-17));
        topGreen.getTransforms().add(new Translate(0, 0, zGreen-17));
        topBlue.getTransforms().add(new Translate(0, 0, zBlue-17));

        Sphere eye1 = new Sphere(1);
        eye1.setMaterial(new PhongMaterial(Color.BLACK));
        eye1.getTransforms().add(new Translate(-2, -6, -20));

        Sphere eye2 = new Sphere(1);
        eye2.setMaterial(new PhongMaterial(Color.BLACK));
        eye2.getTransforms().add(new Translate(2, -6, -20));

        Cylinder nose = new Cylinder(0.5, 5);
        nose.setMaterial(new PhongMaterial(Color.ORANGE));
        nose.getTransforms().add(new Translate(0, -6, -17));

        Cylinder leftArm = new Cylinder(0.5, 10);
        leftArm.setMaterial(new PhongMaterial(Color.BLACK));
        leftArm.getTransforms().add(new Translate(-10, 0, -10));
        leftArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));

        Cylinder rightArm = new Cylinder(0.5, 10);
        rightArm.setMaterial(new PhongMaterial(Color.BLACK));
        rightArm.getTransforms().add(new Translate(10, 0, -10));
        rightArm.getTransforms().add(new Rotate(90, 0, 0, 0, Rotate.Z_AXIS));

        createAttackObject();

        newGroupFace = new Group(eye1, eye2, nose, leftArm, rightArm);
        newGroupRed = new Group(baseRed, topRed);
        newGroupGreen = new Group(baseGreen, topGreen);
        newGroupBlue = new Group(baseBlue, topBlue);

        groupFace = newGroupFace;
        groupRed = newGroupRed;
        groupGreen = newGroupGreen;
        groupBlue = newGroupBlue;

        groupRed.getTransforms().add(new Translate(0, offsetY, 0));
        groupGreen.getTransforms().add(new Translate(0, offsetY, 0));
        groupBlue.getTransforms().add(new Translate(0, offsetY, 0));

        groupRedAttack.getTransforms().add(new Translate(0, offsetY, 0));
        groupGreenAttack.getTransforms().add(new Translate(0, offsetY, 0));
        groupBlueAttack.getTransforms().add(new Translate(0, offsetY, 0));
    }

    // Snowmanクラスにインポートオブジェクトを読み込むメソッドを追加
    public void createAttackObject() throws Exception {
        // 赤い攻撃オブジェクトを読み込む
        fileRed = new File("src/main/resources/3dmodels/redAttackRadius100.obj");
        ObjImporter importerRed = new ObjImporter();
        Model3D modelRed = importerRed.load(fileRed.toURI().toURL());
        Group importedRedObject = modelRed.getRoot();
        importedRedObject.getTransforms().add(new Scale(attackSize, attackSize, 1.0));
        groupRedAttack.getChildren().add(importedRedObject);

        // 緑の攻撃オブジェクトを読み込む
        fileGreen = new File("src/main/resources/3dmodels/greenAttackRadius100.obj");
        ObjImporter importerGreen = new ObjImporter();
        Model3D modelGreen = importerGreen.load(fileGreen.toURI().toURL());
        Group importedGreenObject = modelGreen.getRoot();
        importedGreenObject.getTransforms().add(new Scale(attackSize, attackSize, 1.0));
        groupGreenAttack.getChildren().add(importedGreenObject);

        // 青い攻撃オブジェクトを読み込む
        fileBlue = new File("src/main/resources/3dmodels/blueAttackRadius100.obj");
        ObjImporter importerBlue = new ObjImporter();
        Model3D modelBlue = importerBlue.load(fileBlue.toURI().toURL());
        Group importedBlueObject = modelBlue.getRoot();
        importedBlueObject.getTransforms().add(new Scale(attackSize, attackSize, 1.0));
        groupBlueAttack.getChildren().add(importedBlueObject);
    }

    public void setAttackObjectScale(double scaleX, double scaleY, double scaleZ) {
        for (Node node : groupRedAttack.getChildren()) {
            if (node instanceof Group) {
                node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
            }
        }
        for (Node node : groupGreenAttack.getChildren()) {
            if (node instanceof Group) {
                node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
            }
        }
        for (Node node : groupBlueAttack.getChildren()) {
            if (node instanceof Group) {
                node.getTransforms().add(new Scale(scaleX, scaleY, scaleZ));
            }
        }
    }

    public Group getGroupFace() {
        return groupFace;
    }
    public Group getGroupRed() {
        return groupRed;
    }
    public Group getGroupGreen() {
        return groupGreen;
    }
    public Group getGroupBlue() {
        return groupBlue;
    }
    public Group getGroupRedAttack() {
        return groupRedAttack;
    }
    public Group getGroupGreenAttack() {
        return groupGreenAttack;
    }
    public Group getGroupBlueAttack() {
        return groupBlueAttack;
    }

    public void changeAttack() {
        this.isAttack = !this.isAttack;
    }

    public void update() {
        if(Objects.equals(this.str, "red")) {
            zRed = showZ; zGreen = clearZ; zBlue = clearZ;
        }
        else if(Objects.equals(this.str, "green")) {
            zRed = clearZ; zGreen = showZ; zBlue = clearZ;
        }
        else {
            zRed = clearZ; zGreen = clearZ; zBlue = showZ;
        }

        if (isAttack) {
            if (Objects.equals(str, "red")) {
                redAttackZ = showZ; greenAttackZ = clearZ; blueAttackZ =clearZ;
            } else if (Objects.equals(str, "green")) {
                greenAttackZ = showZ; redAttackZ = clearZ; blueAttackZ = clearZ;
            } else if (Objects.equals(str, "blue")) {
                blueAttackZ = showZ; redAttackZ = clearZ; greenAttackZ = clearZ;
            }
        }
        else{
            redAttackZ = clearZ; greenAttackZ = clearZ; blueAttackZ =clearZ;
        }

        this.groupFace.getTransforms().setAll(
                new Translate(x, y, showZ),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );

        this.groupRed.getTransforms().setAll(
                new Translate(x, y, zRed),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );
        this.groupGreen.getTransforms().setAll(
                new Translate(x, y, zGreen),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );
        this.groupBlue.getTransforms().setAll(
                new Translate(x, y, zBlue),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );

        this.groupRedAttack.getTransforms().setAll(
                new Translate(x, y, redAttackZ),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );
        this.groupGreenAttack.getTransforms().setAll(
                new Translate(x, y, greenAttackZ),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );
        this.groupBlueAttack.getTransforms().setAll(
                new Translate(x, y, blueAttackZ),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );
    }

    //getter and setter
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return showZ;
    }
    public double getzRed() {
        return zRed;
    }
    public double getzGreen() {
        return zGreen;
    }
    public double getzBlue() {
        return zBlue;
    }
    public double getRotate() {
        return rotate;
    }

    public List<Group> getGroups() {
        return Arrays.asList(groupFace, groupRed, groupGreen, groupBlue, groupRedAttack, groupGreenAttack, groupBlueAttack);
    }

    public void setX(double newX) {
        this.x = newX;
    }
    public void setY(double newY) {
        this.y = newY;
    }
    public void setRotate(double newR) {
        this.rotate = newR;
    }
    public void setType(String newType) {
        this.str = newType;
    }
    public void setAttack(boolean isActive) {
        this.isAttack = isActive;
    }
}