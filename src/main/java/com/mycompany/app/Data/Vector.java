package com.mycompany.app.Data;

import java.util.ArrayList;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Vector {

    ArrayList<Double> values;

    public Vector(){
        values = new ArrayList<>();
    }

    public Vector(ArrayList<Double> values) {
        this.values = values;
    }

    public static Vector fromString(String line) {
        String parts[] = line.split(",");

        if ( ! line.matches("([0-9],)+")  || parts.length  < 2) {
            throw new StringIndexOutOfBoundsException();
        }
        Vector vect = new Vector();
        for (int i = 0; i < parts.length; i++) {
            vect.values.add(Double.valueOf(parts[i]));
        }

        return vect;
    }

    public void addValue(double val) {
        values.add(Double.valueOf(val));
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "values=" + values +
                '}';
    }
}
