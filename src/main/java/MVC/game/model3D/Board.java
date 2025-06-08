package MVC.game.model3D;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Board {
    private Box board;
    private double width = 100000;
    private double height = 100000;
    private double depth = 1;
    private double translateZ = 10;
    private Color color = Color.rgb(194, 178, 128);

    public Board() {
        board = new Box(width, height, depth);
        board.setTranslateZ(translateZ);
        board.setMaterial(new PhongMaterial(color));
    }

    public Box getBoard() {
        return board;
    }
}