package com.mycompany.app;

import com.mycompany.app.NN.NetworkArrays;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 11/4/17.
 */
public class NetworkArraysTest {

    @Test
    public void calculateOutput() {
        ArrayList<Float> inputVals = new ArrayList<>(Arrays.asList(Float.valueOf(0.05f), Float.valueOf(0.1f)));
        ArrayList<Float> outputVals = new ArrayList<>(Arrays.asList(Float.valueOf(0.01f), Float.valueOf(0.99f)));

        NetworkArrays net = generateNetwork();
        net.addInput(inputVals);
        net.addOutput(outputVals);
        net.feedForward();

        assertEquals(0.593269992, net.getOutputs().get(1)[1], 0.00001);
        assertEquals(0.596884378, net.getOutputs().get(1)[2], 0.00001);
        assertEquals(0.75136507, net.getOutputs().get(2)[1], 0.00001);
        assertEquals(0.772928465, net.getOutputs().get(2)[2], 0.00001);
        net.calculateError();
        assertEquals(0.298371109, net.getTotalError(), 0.00001);

        net.calculateDeltas();
        net.backPropagation();

        assertEquals("w5", 0.35891648, net.getWeights().get(1)[1][1][0], 0.00001);
        assertEquals("w6", 0.408666186, net.getWeights().get(1)[2][1][0], 0.00001);
        assertEquals("w7", 0.511301270, net.getWeights().get(1)[1][2][0], 0.00001);
        assertEquals("w8", 0.561370121, net.getWeights().get(1)[2][2][0], 0.00001);

        assertEquals("w1", 0.149780716, net.getWeights().get(0)[1][1][0], 0.00001);
        assertEquals("w2", 0.19956143, net.getWeights().get(0)[2][1][0], 0.00001);
        assertEquals("w3", 0.24975114, net.getWeights().get(0)[1][2][0], 0.00001);
        assertEquals("w4", 0.29950229, net.getWeights().get(0)[2][2][0], 0.00001);
    }

    private NetworkArrays generateNetwork() {
        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(2);
        topology.add(2);
        topology.add(2);

        NetworkArrays net = new NetworkArrays(topology);
        net.getWeights().get(0)[0][1][0] = .35f;
        net.getWeights().get(0)[0][2][0] = .35f;
        net.getWeights().get(0)[1][1][0] = .15f;
        net.getWeights().get(0)[1][2][0] = .25f;
        net.getWeights().get(0)[2][1][0] = .20f;
        net.getWeights().get(0)[2][2][0] = .30f;
        net.getWeights().get(1)[0][1][0] = .60f;
        net.getWeights().get(1)[0][2][0] = .60f;
        net.getWeights().get(1)[1][1][0] = .40f;
        net.getWeights().get(1)[1][2][0] = .50f;
        net.getWeights().get(1)[2][1][0] = .45f;
        net.getWeights().get(1)[2][2][0] = .55f;
        return net;
    }

}
