package com.mycompany.app.NN;

import java.io.Serializable;

import static java.lang.Math.sqrt;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Neuron  implements Serializable {
    private static final long serialVersionUID = -5364263379896718318L;

    int myIndex;
    double output;
    double[] weights = null;
    double[] deltaWeights = null;
    double delta;


    public Neuron(int myIndex, int outputs) {
        weights = new double[outputs+1];
        deltaWeights = new double[outputs+1];
        this.myIndex = myIndex;
        for (int i = 0; i < outputs+1; i++ )
            weights[i] = Util.randomDoubleBetween(-(3/sqrt(outputs)), (3/sqrt(outputs)));

    }

    public void calculateOutput(Layer prevLayer) {
        double u = 0;

        for(int i = 0; i < prevLayer.getNeurons().size(); i++) {
            double weight = prevLayer.getNeuron(i).getWeight(myIndex);
            double input = i == 0 ? 1 : prevLayer.getNeuron(i).getOutput();
            u += weight * input;
        }

        output = activationFunction(u);
    }

    public double activationFunction(double u) {
        // sigmoid funkcia - hyperbolicky tangens
//        return tanh(Util.lambda * u);
        return 1/(1+Math.exp( -(Util.lambda) * u));
    }

    //SETTERS AND GETTERS
    public void setOutput(double output) {
        this.output = output;
    }

    public double getWeight(int index) {
        return weights[index];
    }

    public double getOutput() {
        return output;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public double getDelta() {
        return delta;
    }

    public void updateWeightDelta(Layer prevLayer) {
        for(int n = 1; n < prevLayer.getNeurons().size(); n++) {
            double deltaWeight = Util.micro * delta * prevLayer.getNeuron(n).getOutput();
            prevLayer.getNeuron(n).setDeltaWeight(myIndex, deltaWeight);
            prevLayer.getNeuron(n).updateWeight(myIndex);
        }
    }

    public void setDeltaWeight(int index, double deltaWeight) {
        deltaWeights[index] = deltaWeight;
    }

    public void setWeight(int index, double weight) {
        weights[index] = weight;
    }


    public void updateWeight(int index) {
        weights[index] += deltaWeights[index];
    }

    @Override
    public String toString() {
        return String.valueOf(output);
    }
}
