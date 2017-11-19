package com.mycompany.app.NN;

import com.mycompany.app.Data.Data;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/21/17.
 */
public class Util {
    public static float lambda = 1.0f;
    public static float micro = 0.1f;

    public static float randomFloatBetween(float lowerBound, float upperBound) {
        return (float) ThreadLocalRandom.current().nextDouble(lowerBound, upperBound);
    }

    public static float randomIntBetween(int lowerBound, int upperBound) {
        return ThreadLocalRandom.current().nextInt(lowerBound, upperBound+1);
    }

    public static NetworkArrays createAndTrainArrays(Data data) {

        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(data.getData().get(0).getValues().size()));
        topology.add(Integer.valueOf( data.getData().get(0).getValues().size() / 2));
        topology.add(Integer.valueOf(data.getData().get(0).getValues().size()));

        NetworkArrays net = new NetworkArrays(topology);
        net.setCycles(300);
        net.train(data);
        return net;
    }

    public static Network createAndTrain(Data data) {

        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(data.getData().get(0).getValues().size()));
        topology.add(Integer.valueOf( data.getData().get(0).getValues().size() / 2));
        topology.add(Integer.valueOf(data.getData().get(0).getValues().size()));

        Network net = new Network(topology);
        net.setCycles(300);
//        net.train(data);
        return net;
    }
}
