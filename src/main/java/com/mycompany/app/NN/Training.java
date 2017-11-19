package com.mycompany.app.NN;

import com.mycompany.app.Data.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 11/19/17.
 */
public class Training extends Thread{
    private Network net;
    private Data data;
    private HBox outputImgHBox;
    private HBox buttonsBox;
    private Button buttonStop;
    private Tab testTab;


    public void setButtonStop(Button buttonStop) {
        this.buttonStop = buttonStop;
    }

    public void setButtonsBox(HBox buttonsBox) {
        this.buttonsBox = buttonsBox;
    }


    public void setTestTab(Tab testTab) {
        this.testTab = testTab;
    }

    public void setNet(Network net) {
        this.net = net;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setOutputImgHBox(HBox outputImgHBox) {
        this.outputImgHBox = outputImgHBox;
    }

    public void run(){
        net.train(data, outputImgHBox);
        buttonsBox.setDisable(false);
        buttonStop.setDisable(true);
        testTab.setDisable(false);
    }
}
