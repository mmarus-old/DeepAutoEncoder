package com.mycompany.app.NN;

import java.io.Serializable;

import static java.lang.Math.sqrt;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Neuron  implements Serializable {
    private static final long serialVersionUID = -5364263379896718318L;

    int myIndex;
    float output;
    float[] weights = null;
    float[] deltaWeights = null;
    float delta;


    public Neuron(int myIndex, int outputs) {
        float max = (float) (3/sqrt(outputs));
        max = (max < 0.5 ) ? max : 0.5f;

        weights = new float[outputs+1];
        deltaWeights = new float[outputs+1];
        this.myIndex = myIndex;
        for (int i = 0; i < outputs+1; i++ )
            weights[i] = Util.randomFloatBetween(-max, max);

    }

    public void calculateOutput(Layer prevLayer) {
        float u = 0;

        for(int i = 0; i < prevLayer.getNeurons().size(); i++) {
            float weight = prevLayer.getNeuron(i).getWeight(myIndex);
            float input = i == 0 ? 1 : prevLayer.getNeuron(i).getOutput();
            u += weight * input;
        }

        output = activationFunction(u);
    }

    public float activationFunction(float u) {
        // sigmoid funkcia - hyperbolicky tangens
//        return tanh(Util.lambda * u);
        double pow = Math.exp( -(Util.lambda) * u);
        float res = (float) 1/(1+ (float) pow);
//        float res = (float) 1/(1+Math.exp( -(Util.lambda) * u));
        return res;
    }

    //SETTERS AND GETTERS
    public void setOutput(float output) {
        this.output = output;
    }

    public float getWeight(int index) {
        return weights[index];
    }

    public float getOutput() {
        return output;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void updateWeightDelta(Layer prevLayer) {
        for(int n = 1; n < prevLayer.getNeurons().size(); n++) {
            float deltaWeight = Util.micro * delta * prevLayer.getNeuron(n).getOutput();
            prevLayer.getNeuron(n).setDeltaWeight(myIndex, deltaWeight);
            prevLayer.getNeuron(n).updateWeight(myIndex);
        }
    }

    public void setDeltaWeight(int index, float deltaWeight) {
        deltaWeights[index] = deltaWeight;
    }

    public void setWeight(int index, float weight) {
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
