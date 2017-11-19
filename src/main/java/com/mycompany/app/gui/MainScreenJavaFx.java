package com.mycompany.app.gui;

import com.mycompany.app.Data.Data;
import com.mycompany.app.NN.Network;
import com.mycompany.app.NN.NetworkArrays;
import com.mycompany.app.NN.Util;
import com.mycompany.app.util.Image;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/30/17.
 */
public class MainScreenJavaFx extends Application {
    ImageView iv1 = new ImageView();
    ImageView iv2 = new ImageView();
    ImageView iv3 = new ImageView();
    String workDir = "/home/mmarusic/MyDevel/DeepAutoEncoder/24x24/";
    final Label labelSelectedDirectory = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Hello World!");

        Button btnOpenDirectoryChooser = new Button();
        btnOpenDirectoryChooser.setText("Open DirectoryChooser");
        btnOpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory =
                        directoryChooser.showDialog(stage);

                if(selectedDirectory == null){
                    labelSelectedDirectory.setText("No Directory selected");
                }else{
                    labelSelectedDirectory.setText(selectedDirectory.getAbsolutePath());
                }
            }
        });


//        try {
//            BufferedImage img;
//            img = Image.readImage("batman.jpg");
//            iv1.setImage(SwingFXUtils.toFXImage(img, null));
//            iv2.setImage(SwingFXUtils.toFXImage(img, null));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setFill(Color.WHITE);
        HBox box = new HBox();

        box.getChildren().add(iv1);
        box.getChildren().add(iv2);
        box.getChildren().add(iv3);
        box.getChildren().add(btnOpenDirectoryChooser);
        box.getChildren().add(labelSelectedDirectory);
        root.getChildren().add(box);

        stage.setTitle("ImageView");
        stage.setWidth(415);
        stage.setHeight(200);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }


//    public void train(String fileName) throws IOException {
//        //Load the NN
//
//        //Train the images
//        //Save the NN every 100cycles
//        //Save the images recognized by nn at the end
//
//
//        iv1.setImage(SwingFXUtils.toFXImage(Image.readImage(fileName), null));
//        ArrayList<Float> imageAsFloats = Image.getImgAsFloats(fileName);
//        Data data = new Data(imageAsFloats.size());
//        data.addVector(imageAsFloats);
//        data.addVector(imageAsFloats);
//        data.addVector(imageAsFloats);
//
//        long startTime;
//        long estimatedTime;
//        startTime = System.currentTimeMillis();
//        System.out.println("Start training.");
//        Network net = Util.createAndTrain(data);
//        System.out.println("Training finshed.");
//        estimatedTime = System.currentTimeMillis() - startTime;
//        System.out.println("OOP NN time = " + estimatedTime);
//        BufferedImage img2 = net.getOutputAsImage();
//        iv2.setImage(SwingFXUtils.toFXImage(img2, null));
//
////        startTime = System.currentTimeMillis();
////        System.out.println("Start training.");
////        NetworkArrays net2 = Util.createAndTrainArrays(data);
////        System.out.println("Training finshed.");
////        estimatedTime = System.currentTimeMillis() - startTime;
////        System.out.println("NON OOP NN time = " + estimatedTime);
////        BufferedImage img3 = net2.getOutputAsImage();
////        iv3.setImage(SwingFXUtils.toFXImage(img3, null));
//    }
//
////    {
////        long startTime = System.currentTimeMillis();
////        Network net = new Network(topology);
////        net.setCycles(1000);
////        net.train(data);
////        long estimatedTime = System.currentTimeMillis() - startTime;
////        System.out.println("OOP NN time = " + estimatedTime);
////
////        startTime = System.currentTimeMillis();
////        NetworkArrays net2 = new NetworkArrays(topology);
////        net2.setCycles(1000);
////        net2.train(data);
////        estimatedTime = System.currentTimeMillis() - startTime;
////        System.out.println("NON OOP NN time = " + estimatedTime);
////    }

}
