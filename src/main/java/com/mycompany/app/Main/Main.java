package com.mycompany.app.Main;

import com.mycompany.app.Data.Data;
import com.mycompany.app.Data.Vector;
import com.mycompany.app.NN.Network;
import com.mycompany.app.NN.NetworkArrays;
import com.mycompany.app.util.Image;

import javax.imageio.ImageIO;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//TODO: nacitavanie obrazku
//TODO: x*y prva vrstva, mozno *3 (rgb)


/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Network net = createAndTrainImages();
        save(net);
//        NetworkArrays net2 = loadArrays();


//        createAndTrain();

        //save the images;


//        for( String arg : args) {
//            if(arg.equals("-i")) {
//                ArrayList<Float> imageAsFloats = Image.getImgAsFloats("batman-24x24.jpg");
//                Data data = new Data(imageAsFloats.size());
//                data.addVector(imageAsFloats);
//                data.addVector(imageAsFloats);
//                data.addVector(imageAsFloats);
//                System.out.println("Start training.");
////                net = Util.createAndTrain(data);
//                System.out.println("Training finshed.");
//
//                int i = 1;
//                return;
//            }
//            if(arg.equals("-t")) {
//                net = createAndTrain();
//            }
//            if(arg.equals("-s")) {
//                save(net);
//            }
//            if(arg.equals("-l")) {
//                net = load();
//            }
//            if(arg.equals("-r")) {
//                readAndEvaluateInput(net);
//            }
//        }

        int i = 1;
    }


    private static void readAndEvaluateInput(Network net) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String line = "";
            while (! ( line = reader.readLine() ).matches("([eE]xit|q)")) {
                Vector vect = Vector.fromString(line);
                net.evaluateInput(vect, vect);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Network load() {
        return Network.loadFromFile("file.txt");
    }

    private static NetworkArrays loadArrays() {
        return NetworkArrays.loadFromFile("file.txt");
    }

    private static void save(Network net) {
        Network.save(net, "file.txt");
    }

    private static void save(NetworkArrays net) {
        NetworkArrays.save(net, "file.txt");
    }



    private static Network createAndTrain() {
        int imgSize = 1000;

        int vectSize = imgSize;
        Data data = new Data(vectSize);
        data.generateData(8);

        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(vectSize));
        topology.add(Integer.valueOf(vectSize));
        topology.add(Integer.valueOf(vectSize));

        long startTime = System.currentTimeMillis();
        long estimatedTime = System.currentTimeMillis() - startTime;

        startTime = System.currentTimeMillis();
        Network net = new Network(topology);
        net.setCycles(1000);
//        net.train(data);
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("OOP NN time = " + estimatedTime);

//        startTime = System.currentTimeMillis();
//        NetworkArrays net2 = new NetworkArrays(topology);
//        net2.setCycles(1000);
//        net2.train(data);
//        estimatedTime = System.currentTimeMillis() - startTime;
//        System.out.println("NON OOP NN time = " + estimatedTime);

        return net;
    }


    private static Network createAndTrainImages() {
        int a = 24;
        int imgSize = a*a;
        int imgSizePlusOne = imgSize + a;
        Data data = new Data(imgSize);
        String directory = a+"x"+a+"/";
        System.out.println(directory);
        List<String> imageNames = Arrays.asList("batman", "cat", "einstein", "flower", "glass", "lena", "rose", "tiger");
//        List<String> imageNames = Arrays.asList("batman-24x24", "batman-24x24");

        try {
            for ( String file : imageNames) {
                data.addVector(Image.getImgAsFloats(directory+file+".jpg"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(imgSize));
        topology.add(Integer.valueOf(imgSizePlusOne));
        topology.add(Integer.valueOf(imgSizePlusOne/2));
        topology.add(Integer.valueOf(imgSizePlusOne/4));
        topology.add(Integer.valueOf(imgSizePlusOne/8));
        topology.add(Integer.valueOf(imgSizePlusOne/4));
        topology.add(Integer.valueOf(imgSizePlusOne/2));
        topology.add(Integer.valueOf(imgSizePlusOne));
        topology.add(Integer.valueOf(imgSize));

        long startTime;
        long estimatedTime;

        startTime = System.currentTimeMillis();
        Network net2 = new Network(topology);
        net2.setCycles(20000);
//        net2.train(data);
        estimatedTime = System.currentTimeMillis() - startTime;
        System.out.println("NON OOP NN time = " + estimatedTime);


//        try {
//            for ( int i = 0; i < data.getData().size(); i++) {
//                net2.evaluateInput(data.getData().get(i), data.getData().get(i));
//
//                File outputfile = new File(directory+imageNames.get(i) + "-OUTPUT.jpg");
////                ImageIO.write(net2.getOutputAsImage(), "jpg", outputfile);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return net2;
    }



}
