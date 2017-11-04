package com.mycompany.app.gui;

import com.mycompany.app.Data.Data;
import com.mycompany.app.Data.Vector;
import com.mycompany.app.NN.Network;
import com.mycompany.app.NN.Util;
import com.mycompany.app.util.Image;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/30/17.
 */
public class MainScreenJavaFx extends Application {
    ImageView iv1 = new ImageView();
    ImageView iv2 = new ImageView();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    train("batman.jpg");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            BufferedImage img;
            img = Image.readImage("batman.jpg");
            iv1.setImage(SwingFXUtils.toFXImage(img, null));
            iv2.setImage(SwingFXUtils.toFXImage(img, null));

        } catch (IOException e) {
            e.printStackTrace();
        }


        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);
        HBox box = new HBox();
        box.getChildren().add(iv1);
        box.getChildren().add(iv2);
        box.getChildren().add(btn);
        root.getChildren().add(box);

        stage.setTitle("ImageView");
        stage.setWidth(415);
        stage.setHeight(200);
        stage.setScene(scene);
        stage.sizeToScene();
        train("batman-24x24.jpg");
        stage.show();
    }


    public void train(String fileName) throws IOException {
        iv1.setImage(SwingFXUtils.toFXImage(Image.readImage(fileName), null));
        ArrayList<Double> imageAsDoubles = Image.getImgAsDoubles(fileName);
        Data data = new Data(imageAsDoubles.size());
        data.addVector(imageAsDoubles);
        data.addVector(imageAsDoubles);
        data.addVector(imageAsDoubles);
        System.out.println("Start training.");
        Network net = Util.createAndTrain(data);
        System.out.println("Training finshed.");
        BufferedImage img2 = net.getOutputAsImage();
        iv2.setImage(SwingFXUtils.toFXImage(img2, null));
    }

}
