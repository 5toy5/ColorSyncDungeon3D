module test {
    requires opencv;
    requires org.fxyz3d.importers;

    requires javafx.controls;
    requires javafx.fxml;

    opens MVC.game to javafx.graphics;
}
