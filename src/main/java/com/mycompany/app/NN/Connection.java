package com.mycompany.app.NN;

import java.io.Serializable;

import static java.lang.Math.sqrt;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/21/17.
 */
public class Connection  implements Serializable {
    private static final long serialVersionUID = -5870427198177519107L;

    private double weight;
    private double deltaWeight;


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getDeltaWeight() {
        return deltaWeight;
    }

    public void setDeltaWeight(double deltaWeight) {
        this.deltaWeight = deltaWeight;
    }

    public Connection(int outputs) {
        weight = Util.randomDoubleBetween(-(3/sqrt(outputs)), (3/sqrt(outputs)));
    }

    public void updateWeight() {
        weight += deltaWeight;
    }
}
