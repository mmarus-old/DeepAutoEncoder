package com.mycompany.app.NN;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Layer  implements Serializable {
    private static final long serialVersionUID = 3720276874856303338L;

    private ArrayList<Neuron> neurons = new ArrayList<>();


    public Layer(Integer neuronCount, Integer neuronOutputCount) {
        addBias(neuronOutputCount);

        for (int i = 1; i <= neuronCount.intValue(); i++) {
            neurons.add(new Neuron(i, neuronOutputCount.intValue()));
        }

    }

    public void addBias(Integer neuronOutputCount) {
        Neuron bias = new Neuron(0, neuronOutputCount.intValue());
        bias.setOutput(1);
        neurons.add(bias);
    }

    public Neuron getNeuron(int index) {
        return neurons.get(index);
    }

    public ArrayList<Neuron> getNeurons() {
        return neurons;
    }
}
