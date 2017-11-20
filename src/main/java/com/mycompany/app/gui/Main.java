package com.mycompany.app.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL res = getClass().getResource("/fxml/scene.fxml");
        Parent root = FXMLLoader.load(res);
        primaryStage.setTitle("Deep Auto Encoder");
        primaryStage.setScene(new Scene(root, 1200, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Logger rootLog = Logger.getLogger("");
        rootLog.setLevel( Level.INFO );
        rootLog.getHandlers()[0].setLevel( Level.INFO );
        launch(args);
    }
}
