package com.mycompany.app.NN;

import com.mycompany.app.Data.Data;
import com.mycompany.app.Data.Vector;
import com.mycompany.app.util.Image;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import static java.lang.Math.sqrt;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 11/4/17.
 */
public class NetworkArrays implements Serializable {

    private static final long serialVersionUID = 6489920816640250625L;

    public ArrayList<float[]> getOutputs() {
        return outputs;
    }

    public ArrayList<float[][][]> getWeights() {
        return weights;
    }

    public float getTotalError() {
        return totalError;
    }


    ArrayList<float[]> outputs = new ArrayList<>();
    ArrayList<float[][][]> weights = new ArrayList<>();     //neurony * spojenie * 2 //(weight, deltaWeight)
    ArrayList<float[]> deltas = new ArrayList<>();     //neurony * spojenie * 2 //(weight, deltaWeight)
    float totalError = 0;
    int cycles = 0;
    ArrayList<Float> expectedOutput;


    public NetworkArrays(ArrayList<Integer> topology) {


        topology.add(Integer.valueOf(1));

        for (int i = 0; i < topology.size() - 1; i++) {
            int neurons = 1 + topology.get(i).intValue();
            int neuronsInNextLayer = 1 + topology.get(i+1).intValue();
            outputs.add(new float[neurons]);
            deltas.add(new float[neurons]);
            weights.add(new float[neurons][neuronsInNextLayer][2]);

            float maxWeight = (float) (3/sqrt(neuronsInNextLayer));
            maxWeight = (maxWeight < 0.5 ) ? maxWeight : 0.5f;

            for (int j = 0; j < neurons; j++) {
                for (int k = 0; k < neuronsInNextLayer; k++) {
                    weights.get(i)[j][k][0] = Util.randomFloatBetween(-maxWeight, maxWeight);
                }
            }
        }
    }

    public void train(Data data) {
        int count = 0;
        totalError = 1;
        while (totalError > 0.001 && count <= cycles) {
            totalError = 0;
            data.getData().forEach(vector -> {
                addInput(vector.getValues());
                addOutput(vector.getValues());
                feedForward();
                calculateError();
                calculateDeltas();
                backPropagation();
            });

            if(count % 100 == 0) {
                System.out.println("cycle: " + count + " error: " + String.format("%.9f", totalError));
                NetworkArrays.save(this, "temporaryNetwork.file");
            }
            count++;
        }
    }


    public void addInput(ArrayList<Float> values) {
        float[] firstLayerOutputs = outputs.get(0);

        assert(firstLayerOutputs.length-1 == values.size());

        firstLayerOutputs[0] = 1; // bias
        for (int i = 0; i < values.size(); i++) {
            firstLayerOutputs[i+1] = values.get(i).floatValue();
        }
    }

    public void addOutput(ArrayList<Float> values) {
        expectedOutput = values;
    }

    public void feedForward() {
        for(int l = 1; l < outputs.size() ; l++){
            float[] currentLayerOuts = outputs.get(l);
            float[] prevLayerOuts = outputs.get(l-1);
            float[][][] prevLayerWeights = weights.get(l-1);

            for (int n = 1; n < currentLayerOuts.length; n++) {
                float u = 0;

                for(int i = 0; i < prevLayerOuts.length; i++) {
                    float weight = prevLayerWeights[i][n][0];
                    float input = i == 0 ? 1 : prevLayerOuts[i];
                    u += weight * input;
                }
                currentLayerOuts[n] = activationFunction(u);
            }
        }
    }

    public float activationFunction(float u) {
        // sigmoid funkcia - hyperbolicky tangens
//        return tanh(Util.lambda * u);
        double pow = Math.exp( -(Util.lambda) * u);
        float res = (float) 1/(1+ (float) pow);
//        float res = (float) 1/(1+Math.exp( -(Util.lambda) * u));
        return res;
    }

    public void calculateError() {
        float outputError = 0;
        int lastLayerIndex = outputs.size()-1;
        float[] lastLayer = outputs.get(lastLayerIndex);

        float difference;
        for(int i = 1; i < lastLayer.length; i++) {
            float expected = getExpectedOutputValueAt(i);
            float actual = lastLayer[i];

            difference = expected - actual;
            outputError += 0.5 * (difference * difference);
            float delta = (difference) * Util.lambda * actual * (1 - actual);
//            float delta = Util.lambda * (1 - getOutputValueAt(i) * getOutputValueAt(i));

            deltas.get(lastLayerIndex)[i] = delta;
            int iiii = 1;
        }
        totalError += outputError;
    }

    private float getExpectedOutputValueAt(int index) {
        //-1 bcs there is not the bias here
        return expectedOutput.get(index - 1 ).floatValue();
    }

    public void calculateDeltas() {
        for (int l = outputs.size()-2; l > 0; l--) {
            float[] currentLayerOuts = outputs.get(l);
            float[][][] currentLayerWeights = weights.get(l);
            float[] nextLayerOuts = outputs.get(l+1);
            float[] nextLayerDeltas = deltas.get(l+1);
            float[] currentLayerDeltas = deltas.get(l);

            for (int n = 1; n < currentLayerOuts.length; n++) {
                float sum = 0.0f;

                for (int i = 1; i < nextLayerOuts.length; i++) {
                    sum += nextLayerDeltas[i] * currentLayerWeights[n][i][0];
                }

                currentLayerDeltas[n] = sum * Util.lambda * currentLayerOuts[n] * (1 - currentLayerOuts[n]);
            }
        }
    }

    public void backPropagation() {
        for(int l = 1; l < outputs.size(); l++){
            float[] currentLayerOuts = outputs.get(l);
            float[] currentLayerDeltas = deltas.get(l);
            float[] prevLayerOuts = outputs.get(l-1);
            float[][][] prevLayerWeights = weights.get(l-1);

            for (int n = 1; n < currentLayerOuts.length; n++) {

                for(int i = 1; i < prevLayerOuts.length; i++) {

                    float deltaWeight = Util.micro * currentLayerDeltas[n] * prevLayerOuts[i];
                    prevLayerWeights[i][n][1] = deltaWeight;
                    prevLayerWeights[i][n][0] += prevLayerWeights[i][n][1];
                }
            }
        }
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public BufferedImage getOutputAsImage() {
        int side = (int) sqrt(outputs.get(0).length);
        return Image.getFloatsAsGrayImage(outputs.get(outputs.size()-1), side, side);
    }

    public static void save(NetworkArrays net, String fileName) {
        ObjectOutputStream lOutputStream = null;
        try {
            FileOutputStream os = new FileOutputStream(fileName);
            lOutputStream = new ObjectOutputStream(os);
            lOutputStream.writeObject(net);
            os.close();
            lOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static NetworkArrays loadFromFile(String fileName) {
        NetworkArrays net = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            net = (NetworkArrays) in.readObject();
            in.close();
            fileIn.close();
        }catch(IOException i) {
            i.printStackTrace();
        }catch(ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }

        return net;
    }

    public void evaluateInput(Vector inputVals, Vector expectedOutput) {
        totalError = 0;
        addInput(inputVals.getValues());
        addOutput(expectedOutput.getValues());
        feedForward();
        calculateError();

        System.out.println(" error = " + totalError );
    }
}
