package com.mycompany.app.Data;

import com.mycompany.app.NN.Util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Data {

    ArrayList<Vector> data = new ArrayList<>();
    private int vectorSize = 0;

    public int getVectorSize() {
        return vectorSize;
    }

    public void setVectorSize(int vectorSize) {
        this.vectorSize = vectorSize;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    int height = 0;
    int width = 0;

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

    public void addVector(ArrayList<Float> imageAsFloats) {
        data.add(new Vector(imageAsFloats));
    }
}
