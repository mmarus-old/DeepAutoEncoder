package com.mycompany.app.NN;

import java.io.Serializable;

import static java.lang.Math.sqrt;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/21/17.
 */
public class Connection  implements Serializable {
    private static final long serialVersionUID = -5870427198177519107L;

    private float weight;
    private float deltaWeight;



    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(float deltaWeight) {
        this.deltaWeight = deltaWeight;
    }

    public Connection(int outputs) {
        float min = (float) (3/sqrt(outputs));
        weight = Util.randomFloatBetween(-min, min);
    }

    public void updateWeight() {
        weight += deltaWeight;
    }
}
