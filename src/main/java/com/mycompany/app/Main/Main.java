package com.mycompany.app.Main;

import com.mycompany.app.Data.Data;
import com.mycompany.app.Data.Vector;
import com.mycompany.app.NN.Network;
import com.mycompany.app.NN.Util;
import com.mycompany.app.util.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


//TODO: nacitavanie obrazku
//TODO: x*y prva vrstva, mozno *3 (rgb)


/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Network net = null;
        for( String arg : args) {
            if(arg.equals("-i")) {

                ArrayList<Double> imageAsDoubles = Image.getImgAsDoubles("batman-24x24.jpg");
                Data data = new Data(imageAsDoubles.size());
                data.addVector(imageAsDoubles);
                data.addVector(imageAsDoubles);
                data.addVector(imageAsDoubles);
                System.out.println("Start training.");
                net = Util.createAndTrain(data);
                System.out.println("Training finshed.");

                int i = 1;
                return;
            }
            if(arg.equals("-t")) {
                net = createAndTrain();
            }
            if(arg.equals("-s")) {
                save(net);
            }
            if(arg.equals("-l")) {
                net = load();
            }
            if(arg.equals("-r")) {
                readAndEvaluateInput(net);
            }
        }

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

    private static void save(Network net) {
        Network.save(net, "file.txt");

    }

    private static Network createAndTrain() {
        int vectSize = 100;
        Data data = new Data(vectSize);
        data.generateData(vectSize);

        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(vectSize));
        topology.add(Integer.valueOf(vectSize));
        topology.add(Integer.valueOf(vectSize));

        Network net = new Network(topology);
        net.setCycles(100000);
        net.train(data);
        return net;
    }



}
