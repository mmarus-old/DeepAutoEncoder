package com.mycompany.app.Data;

import com.mycompany.app.NN.Util;

import java.util.ArrayList;


/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Data {

    ArrayList<Vector> data = new ArrayList<>();
    private int vectorSize = 0;

    public Data(int vectorSize) { this.vectorSize = vectorSize; }

    public void generateData(int count) {
        for (int i = 0; i < count; i++) {
            data.add(generateRandomVector());
        }
    }

    private Vector generateRandomVector() {
        Vector vect = new Vector();
        for (int i = 0; i < vectorSize; i++) {
            vect.addValue(Util.randomIntBetween(0,1));
        }
        return vect;
    }

    public ArrayList<Vector> getData() {
        return data;
    }

    public void addVector(ArrayList<Double> imageAsDoubles) {
        data.add(new Vector(imageAsDoubles));
    }
}
