package MVC.game.model3D;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import org.fxyz3d.importers.Model3D;
import org.fxyz3d.importers.obj.ObjImporter;

import java.io.File;
import java.util.Objects;

public class ImportedObject {
    double x, y, z, rotate;
    String str;
    Group group;
    File file;

    public ImportedObject(double x, double y, double rotate, String str) throws Exception {
        this.x = x;
        this.y = y;
        this.z = 10;
        this.rotate = rotate;
        this.str = str;
        loadImportedObject();
        update();
    }

    public void loadImportedObject() throws Exception {
        if (Objects.equals(str, "red")) file = new File("src/main/resources/3dmodels/redMonster.obj");
        if (Objects.equals(str, "green")) file = new File("src/main/resources/3dmodels/greenMonster.obj");
        else if (Objects.equals(str, "blue")) file = new File("src/main/resources/3dmodels/blueMonster.obj");
        ObjImporter importer = new ObjImporter();
        Model3D model = importer.load(file.toURI().toURL());
        group = model.getRoot();
    }

    public Group getGroupImportedObject() {
        return group;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

    public void setRotate(double newRotate){
        this.rotate = newRotate;
    }

    public void clear() {
        this.x = 10000; this.y = 10000;
    }

    public void update() {
        this.group.getTransforms().setAll(
                new Translate(x, y, z),
                new Rotate(180, 0, 0, 0, Rotate.X_AXIS),
                new Rotate(rotate, 0, 0, 0, Rotate.Z_AXIS)
        );
    }
}
