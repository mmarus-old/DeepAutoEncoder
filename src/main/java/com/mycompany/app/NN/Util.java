package com.mycompany.app.NN;

import com.mycompany.app.Data.Data;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/21/17.
 */
public class Util {
    public static double lambda = 1.0;
    public static double micro = 0.5;
    public static double randomDoubleBetween(double lowerBound, double upperBound) {
        return ThreadLocalRandom.current().nextDouble(lowerBound, upperBound);
    }

    public static double randomIntBetween(int lowerBound, int upperBound) {
        return ThreadLocalRandom.current().nextInt(lowerBound, upperBound+1);
    }

    public static Network createAndTrain(Data data) {

        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(data.getData().get(0).getValues().size()));
        topology.add(Integer.valueOf( data.getData().get(0).getValues().size() / 2));
        topology.add(Integer.valueOf(data.getData().get(0).getValues().size()));

        Network net = new Network(topology);
        net.setCycles(10000);
        net.train(data);
        return net;
    }
}
