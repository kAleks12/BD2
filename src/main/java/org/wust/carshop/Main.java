package org.wust.carshop;

import org.jdbi.v3.core.Jdbi;
import org.postgresql.core.Utils;
import org.postgresql.ds.PGSimpleDataSource;
import org.wust.carshop.model.Address;
import org.wust.carshop.model.Employee;
import org.wust.carshop.model.Part;
import org.wust.carshop.service.*;
import org.wust.carshop.util.PartPair;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String javaVersion = System.getProperty("java.version");
        String javafxVersion = System.getProperty("javafx.version");
        Label l = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        Scene scene = new Scene(new StackPane(l), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}