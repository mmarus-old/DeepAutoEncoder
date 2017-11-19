package com.mycompany.app.Data;

import java.util.ArrayList;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */
public class Vector {

    ArrayList<Float> values;

    public Vector(){
        values = new ArrayList<>();
    }

    public Vector(ArrayList<Float> values) {
        this.values = values;
    }

    public static Vector fromString(String line) {
        String parts[] = line.split(",");

        if ( ! line.matches("([0-9],)+")  || parts.length  < 2) {
            throw new StringIndexOutOfBoundsException();
        }
        Vector vect = new Vector();
        for (int i = 0; i < parts.length; i++) {
            vect.values.add(Float.valueOf(parts[i]));
        }

        return vect;
    }

    public void addValue(float val) {
        values.add(Float.valueOf(val));
    }

    public ArrayList<Float> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "Vector{" +
                "values=" + values +
                '}';
    }
}
