package com.mycompany.app;

import com.mycompany.app.NN.Network;
import com.mycompany.app.NN.Util;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Math.sqrt;
import static org.junit.Assert.*;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class NetworkTest {
    Logger logger = Logger.getLogger("Test");

    @Test
    public void calculateOutput() {
        ArrayList<Double> inputVals = new ArrayList<>(Arrays.asList(Double.valueOf(0.05), Double.valueOf(0.1)));
        ArrayList<Double> outputVals = new ArrayList<>(Arrays.asList(Double.valueOf(0.01), Double.valueOf(0.99)));

        Network net = generateNetwork();
        net.addInput(inputVals);
        net.addOutput(outputVals);
        net.feedForward();
        assertEquals(0.593269992, net.getLayers().get(1).getNeuron(1).getOutput(), 0.00001);
        assertEquals(0.596884378, net.getLayers().get(1).getNeuron(2).getOutput(), 0.00001);
        assertEquals(0.75136507, net.getLayers().get(2).getNeuron(1).getOutput(), 0.00001);
        assertEquals(0.772928465, net.getLayers().get(2).getNeuron(2).getOutput(), 0.00001);
        net.calculateError();
        assertEquals(0.298371109, net.getTotalError(), 0.00001);
        net.calculateDeltas();
        net.backPropagation();

        assertEquals("w5", 0.35891648, net.getLayers().get(1).getNeuron(1).getWeight(1), 0.00001);
        assertEquals("w6", 0.408666186, net.getLayers().get(1).getNeuron(2).getWeight(1), 0.00001);
        assertEquals("w7", 0.511301270, net.getLayers().get(1).getNeuron(1).getWeight(2), 0.00001);
        assertEquals("w8", 0.561370121, net.getLayers().get(1).getNeuron(2).getWeight(2), 0.00001);

        assertEquals("w1", 0.149780716, net.getLayers().get(0).getNeuron(1).getWeight(1), 0.00001);
        assertEquals("w2", 0.19956143, net.getLayers().get(0).getNeuron(2).getWeight(1), 0.00001);
        assertEquals("w3", 0.24975114, net.getLayers().get(0).getNeuron(1).getWeight(2), 0.00001);
        assertEquals("w4", 0.29950229, net.getLayers().get(0).getNeuron(2).getWeight(2), 0.00001);
    }

    private Network generateNetwork() {
        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(2);
        topology.add(2);
        topology.add(2);

        Network net = new Network(topology);
        net.getLayers().get(0).getNeuron(0).setWeight(1, .35);
        net.getLayers().get(0).getNeuron(0).setWeight(2, .35);

        net.getLayers().get(0).getNeuron(1).setWeight(1,.15);
        net.getLayers().get(0).getNeuron(1).setWeight(2,.25);

        net.getLayers().get(0).getNeuron(2).setWeight(1, .20);
        net.getLayers().get(0).getNeuron(2).setWeight(2,.30);


        net.getLayers().get(1).getNeuron(0).setWeight(1,.60);
        net.getLayers().get(1).getNeuron(0).setWeight(2,.60);

        net.getLayers().get(1).getNeuron(1).setWeight(1,.40);
        net.getLayers().get(1).getNeuron(1).setWeight(2,.50);

        net.getLayers().get(1).getNeuron(2).setWeight(1,.45);
        net.getLayers().get(1).getNeuron(2).setWeight(2,.55);

        return net;
    }


    @Test
    public void topologyCreation() {
        ArrayList<Integer> topology = new ArrayList<>();
        int inputLayerSize = 6;
        int hiddenLayerSize = 3;
        int outputLayerSize = 6;
        topology.add(Integer.valueOf(inputLayerSize));
        topology.add(Integer.valueOf(hiddenLayerSize));
        topology.add(Integer.valueOf(outputLayerSize));


        Network net = new Network(topology);

        assertEquals(3, net.getLayers().size() );
        assertEquals(inputLayerSize +1, net.getLayers().get(0).getNeurons().size() );
        assertEquals(hiddenLayerSize +1, net.getLayers().get(1).getNeurons().size() );
        assertEquals(outputLayerSize +1, net.getLayers().get(2).getNeurons().size() );
    }

    @Test
    public void randomCreation() {
        for (int i = 0; i < 20; i++) {
            double randNum = Util.randomDoubleBetween(-(3/sqrt(100)), (3/sqrt(100)));
            logger.log(Level.INFO, String.valueOf(randNum));
            assertTrue(randNum >= -(3/sqrt(100)) && randNum <= (3/sqrt(100)));
        }
    }



}