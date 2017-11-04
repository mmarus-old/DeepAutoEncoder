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
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */


public class Network implements Serializable {
    private static final long serialVersionUID = -4209177588765477305L;
    ArrayList<Layer> layers = new ArrayList<>();
    ArrayList<Integer> topology;
    double outputError;
    double totalError = 0;
    double micro = 0.05;
    int cycles = 10;
    ArrayList<Double> expectedOutput;


    public void evaluateInput(Vector inputVals, Vector expectedOutput) {
        totalError = 0;
        addInput(inputVals.getValues());
        addOutput(expectedOutput.getValues());
        feedForward();
        calculateError();

        System.out.println("Output: "+ getOutputLayer().getNeurons() + " error = " + totalError );
    }

    private Layer getOutputLayer() {
        return layers.get(layers.size()-1);
    }


    private Layer getInputLayer() {
        return layers.get(0);
    }

    public Network(ArrayList<Integer> topology) {
        this.topology = topology;
        topology.add(Integer.valueOf(1));

        for (int i = 0; i < topology.size() - 1; i++) {
            layers.add(new Layer(topology.get(i), topology.get(i+1)));
        }

    }

//    public void train(ArrayList<int[]> images) {
//        //TODO: skontrolovat velkost vektoru vs topology
//        int count = 0;
//        totalError = 1;
//        while (totalError > 0.02 && count <= cycles) {
//            totalError = 0;
//            images.forEach(vector -> {
//                ArrayList<Double> doubleVector = Arrays.asList(vector);
//                addInput(vector.getValues());
//                addOutput(vector.getValues());
//                feedForward();
//                calculateError();
//                calculateDeltas();
//                backPropagation();
//            });
//
//            if(count % 1000 == 0)
//                System.out.println("cycle: " +count+" error: "+ String.format( "%.5f", totalError ) );
//            count++;
//        }
//    }

    public void train(Data data) {
        //TODO: skontrolovat velkost vektoru vs topology
        int count = 0;
        totalError = 1;
        while (totalError > 0.5 && count <= cycles) {
            totalError = 0;
            data.getData().forEach(vector -> {
                addInput(vector.getValues());
                addOutput(vector.getValues());
                feedForward();
                calculateError();
                calculateDeltas();
                backPropagation();
            });

            if(count % 10 == 0)
                System.out.println("cycle: " +count+" error: "+ String.format( "%.9f", totalError ) );
            count++;
        }
    }

    public void addInput(ArrayList<Double> values) {
        getInputLayer().getNeuron(0).setOutput(1);
        for(int i = 1; i < getInputLayer().getNeurons().size(); i++) {
            getInputLayer().getNeuron(i).setOutput(values.get(i-1).doubleValue());
        }
    }

    public void addOutput(ArrayList<Double> values) {
        expectedOutput = values;
    }

    public void feedForward() {
        for(int l = 1; l < layers.size() ; l++){
            Layer currnetLayer = layers.get(l);
            Layer prevLayer = layers.get(l-1);
            for (int n = 1; n < currnetLayer.getNeurons().size(); n++) {
                currnetLayer.getNeurons().get(n).calculateOutput(prevLayer);
            }
        }

    }

    public void calculateError() {
        outputError = 0;
        double difference;
        double neurons = getOutputLayer().getNeurons().size();
        for(int i = 1; i < getOutputLayer().getNeurons().size(); i++) {
            double expected = getExpectedOutputValueAt(i);
            double actual = getOutputValueAt(i);
            difference = expected - actual;
            outputError += 0.5 * difference * difference;
            double delta = (difference) * Util.lambda * getOutputValueAt(i) * (1 - getOutputValueAt(i));
//            double delta = Util.lambda * (1 - getOutputValueAt(i) * getOutputValueAt(i));
            getOutputLayer().getNeuron(i).setDelta(delta);

            double deeelta = getOutputLayer().getNeuron(i).getDelta();
            int iiii = 1;
        }
        totalError += outputError;
    }

    public void calculateDeltas() {
        for (int l = layers.size()-2; l > 0; l--) {
            Layer currnetLayer = layers.get(l);
            Layer nextLayer = layers.get(l + 1);
            for (int n = 1; n < currnetLayer.getNeurons().size(); n++) {
                double sum = 0.0;

                for (int i = 1; i < nextLayer.getNeurons().size(); i++) {
                    sum += nextLayer.getNeuron(i).getDelta() * currnetLayer.getNeuron(n).getWeight(i);
                }

                currnetLayer.getNeuron(n).setDelta(
                        sum * Util.lambda * currnetLayer.getNeuron(n).getOutput() * (1 - currnetLayer.getNeuron(n).getOutput()));
            }
        }
    }

    public void backPropagation() {
        //calculateWeightDeltas

        for(int l = 1; l < layers.size(); l++){
            Layer currnetLayer = layers.get(l);
            Layer prevLayer = layers.get(l-1);
            for (int n = 1; n < currnetLayer.getNeurons().size(); n++) {
                currnetLayer.getNeuron(n).updateWeightDelta(prevLayer);
            }
        }

    }

    private double getExpectedOutputValueAt(int index) {
        //-1 bcs there is not the bias here
        return expectedOutput.get(index - 1 ).doubleValue();
    }

    public double getOutputValueAt(int index) {
        return getOutputLayer().getNeuron(index).getOutput();
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public double getTotalError() {
        return totalError;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    public static void save(Network net, String fileName) {
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

    public static Network loadFromFile(String fileName) {
        Network net = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            net = (Network) in.readObject();
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

    public ArrayList<Double> getRealOutput() {
        ArrayList<Double> output = new ArrayList<>();

        for ( Neuron neuron :getOutputLayer().getNeurons()) {
            output.add(neuron.output);
        }

        return output;
    }

    public BufferedImage getOutputAsImage() {
        int side = (int) sqrt(getRealOutput().size());
        return Image.getDoublesAsGrayImage(getRealOutput(), side, side);
    }


}
