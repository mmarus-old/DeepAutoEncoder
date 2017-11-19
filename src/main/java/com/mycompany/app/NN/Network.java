package com.mycompany.app.NN;

import com.mycompany.app.Data.Data;
import com.mycompany.app.Data.Vector;
import com.mycompany.app.util.Image;
import com.mycompany.app.util.Rand;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/19/17.
 */


public class Network implements Serializable {
    private static final long serialVersionUID = -4209177588765477305L;
    ArrayList<Layer> layers = new ArrayList<>();
    ArrayList<Integer> topology;
    float totalError = 0;
    int cycles = 10;
    float precision = 0.2f;
    ArrayList<Float> expectedOutput;
    List<Integer> indexes = new ArrayList<Integer>();

    public float getOutputError() {
        return outputError;
    }

    float outputError = 0;



    public void evaluateInput(Vector inputVals, Vector expectedOutput) {
        totalError = 0;
        addInput(inputVals.getValues());
        addOutput(expectedOutput.getValues());
        feedForward();
        calculateError();

        System.out.println("Error = " + totalError );
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

    public void train(Data data, HBox outputImgHBox) {
        outputImgHBox.setSpacing(2);
        indexes.clear();
        for(int i = 0; i < data.getData().size(); i++) {
            indexes.add(Integer.valueOf(i));
        }

        int count = 0;
        totalError = 1;
        while (totalError > precision && count < cycles && !Thread.currentThread().isInterrupted()) {
            totalError = 0;
            Collections.shuffle(indexes);
            for(int index: indexes) {
                addInput(data.getData().get(index).getValues());
                addOutput(data.getData().get(index).getValues());
                feedForward();
                calculateError();
                calculateDeltas();
                backPropagation();
                if(count % 100 == 0 && index <= 8) {
                    BufferedImage img = getOutputAsImage(data.getWidth(), data.getHeight());
                    BufferedImage newImage = new BufferedImage(data.getWidth()*5, data.getHeight()*5, img.getType());
                    Graphics g = newImage.createGraphics();
                    g.drawImage(img, 0, 0, data.getWidth()*5, data.getHeight()*5, null);
                    g.dispose();
                    ImageView imageView = new ImageView();
                    imageView.setImage(SwingFXUtils.toFXImage(newImage, null));
                    Label label = new Label();
                    label.setText("Err: "+String.valueOf(outputError));

                    Platform.runLater(() -> {
                            ((VBox)outputImgHBox.getChildren().get(index)).getChildren().set(0, imageView);
                            ((VBox)outputImgHBox.getChildren().get(index)).getChildren().set(1, label);
                    });
                }
            }

            if(count % 10 == 0) {
                System.out.println("cycle: " +count+" error: "+ String.format( "%.9f", totalError ) );
                Network.save(this, "temporaryNetwork.file");
            }
            count++;
        }
        System.out.println("Done training");
    }

    public void addInput(ArrayList<Float> values) {
        getInputLayer().getNeuron(0).setOutput(1);
        for(int i = 1; i < getInputLayer().getNeurons().size(); i++) {
            getInputLayer().getNeuron(i).setOutput(values.get(i-1).floatValue());
        }
    }

    public void addOutput(ArrayList<Float> values) {
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
        float difference;
        for(int i = 1; i < getOutputLayer().getNeurons().size(); i++) {
            float expected = getExpectedOutputValueAt(i);
            float actual = getOutputValueAt(i);
            difference = expected - actual;
            outputError += 0.5 * difference * difference;
            float delta = (difference) * Rand.lambda * getOutputValueAt(i) * (1 - getOutputValueAt(i));
//            float delta = Rand.lambda * (1 - getOutputValueAt(i) * getOutputValueAt(i));
            getOutputLayer().getNeuron(i).setDelta(delta);
            int iiii = 1;
        }
        totalError += outputError;
    }

    public void calculateDeltas() {
        for (int l = layers.size()-2; l > 0; l--) {
            Layer currnetLayer = layers.get(l);
            Layer nextLayer = layers.get(l + 1);
            for (int n = 1; n < currnetLayer.getNeurons().size(); n++) {
                float sum = 0.0f;

                for (int i = 1; i < nextLayer.getNeurons().size(); i++) {
                    sum += nextLayer.getNeuron(i).getDelta() * currnetLayer.getNeuron(n).getWeight(i);
                }

                currnetLayer.getNeuron(n).setDelta(
                        sum * Rand.lambda * currnetLayer.getNeuron(n).getOutput() * (1 - currnetLayer.getNeuron(n).getOutput()));
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

    private float getExpectedOutputValueAt(int index) {
        //-1 bcs there is not the bias here
        return expectedOutput.get(index - 1 ).floatValue();
    }

    public float getOutputValueAt(int index) {
        return getOutputLayer().getNeuron(index).getOutput();
    }

    public ArrayList<Layer> getLayers() {
        return layers;
    }

    public float getTotalError() {
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

    public ArrayList<Float> getRealOutput() {
        ArrayList<Float> output = new ArrayList<>();

        for ( Neuron neuron :getOutputLayer().getNeurons()) {
            output.add(neuron.output);
        }

        return output;
    }

    public BufferedImage getOutputAsImage(int width, int height) {
        return Image.getFloatsAsGrayImage(getRealOutput(), width, height);
    }


    public void setPrecision(float precision) {
        this.precision = precision;
    }
}
