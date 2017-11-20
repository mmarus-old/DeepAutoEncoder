package com.mycompany.app.gui;

import com.mycompany.app.Data.Data;
import com.mycompany.app.NN.Network;
import com.mycompany.app.NN.Training;
import com.mycompany.app.util.Image;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mycompany.app.util.Rand.randomFloatBetween;
import static com.mycompany.app.util.Rand.randomIntBetween;

public class Controller {
    private static final Logger LOGGER = Logger.getLogger( Controller.class.getName() );
    @FXML private VBox vbox;
    @FXML private Button buttonStop;
    @FXML private Button selectDataButt;
    @FXML private TextField cyclesText;
    @FXML private TextField precisionText;
    @FXML private HBox originalImagesHBox;
    @FXML private HBox buttonsBox;
    @FXML private HBox outputImgHBox;
    @FXML private Tab testTab;
    @FXML private ImageView originalImg;
    @FXML private ImageView realImg;
    @FXML private Label imgErrorLabel;
    @FXML private Button SaveResultButt;
    @FXML private Button trainButt;
    @FXML private Button stepButt;
    @FXML private Label infoText1;
    @FXML private Label infoText2;
    @FXML private Slider noiseSlider;
    @FXML private Label noiseLabel;


    private Network net = null;
    private Data data = null;
    private Training training = null;
    private ArrayList<Float> loadedImageForEval = null;

    public void initialize(){
        File file = new File(getClass().getResource("/24x24/").getFile());
        handleSelectedDataDir(file);

        noiseSlider.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue arg0, Object arg1, Object arg2) {
                int noise = (int) noiseSlider.getValue();
                noiseLabel.textProperty().setValue("Noise "+String.valueOf(noise)  + "%:");
                if (loadedImageForEval != null) {
                    showAndEvaluateImage(noise, loadedImageForEval);
                }

            }
        });

    }

    public void handleSelectData(ActionEvent e) {
        LOGGER.log(Level.INFO, "HandleSelectData");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(getStage());
        handleSelectedDataDir(selectedDirectory);
    }

    private void handleSelectedDataDir(File selectedDirectory) {
        if(selectedDirectory != null){
            selectDataButt.setText(selectedDirectory.getName());
            Data data = loadImagesFromDir(selectedDirectory);
            showInputImages(data);
            initOutputImages();
            createNewNet();
            enableNetFucntionality();
        }
        infoText1.setText("Train or do a step of the training.");
        infoText2.setText("Load image to evaluate by neural net.");
    }

    private void enableNetFucntionality() {
        testTab.setDisable(false);
        trainButt.setDisable(false);
        stepButt.setDisable(false);
    }

    private void createNewNet() {
        ArrayList<Integer> topology = new ArrayList<>();
        topology.add(Integer.valueOf(data.getVectorSize()));
        topology.add(Integer.valueOf(data.getVectorSize()/2));
        topology.add(Integer.valueOf(data.getVectorSize()/4));
        topology.add(Integer.valueOf(data.getVectorSize()/4));
        topology.add(Integer.valueOf(data.getVectorSize()/2));
        topology.add(Integer.valueOf(data.getVectorSize()));

        net = new Network(topology);
        setCyclesAndPrecsion();
    }

    private void setCyclesAndPrecsion() {
        int cycles = cyclesText.getText().matches("\\d+") ? Integer.parseInt(cyclesText.getText()) : 20000;
        net.setCycles(cycles);

        float precision = precisionText.getText().matches("\\d+(\\.\\d+)?") ? Float.parseFloat(precisionText.getText()) : 0.02f;
        net.setPrecision(precision);
    }

    private void initOutputImages() {
        outputImgHBox.getChildren().clear();
        int maxImages = ( data.getData().size() > 8 ) ? 8 : data.getData().size();
        for (int i = 0; i < maxImages; i++) {
            VBox vbox = new VBox();
            vbox.getChildren().add(new ImageView());
            vbox.getChildren().add(new Label());
            outputImgHBox.getChildren().add(vbox);
        }
        Label totalErrLabel = new Label();
        totalErrLabel.setText("Total error: ");
        outputImgHBox.getChildren().add(totalErrLabel);
        Label totalErrNum = new Label();
        totalErrNum.setText("0");
        outputImgHBox.getChildren().add(totalErrNum);
    }

    public Data loadImagesFromDir(final File folder) {
        int imgSize = 0;
        data = null;
        ArrayList<InputStream> files = null;
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        //pozri ci files neni null
        if(jarFile.isFile()) {  // Run with JAR file
            files = new ArrayList<>();
            final JarFile jar;
            try {
                jar = new JarFile(jarFile);
                final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
                while(entries.hasMoreElements()) {
                    final String name = entries.nextElement().getName();
                    String fname = folder.getName() + "/";
                    if (name.contains(fname) && name.contains(".jpg")) { //filter according to the path
                        files.add(getClass().getResourceAsStream("/"+name));
                        System.out.println(name);
                    }
                }
                jar.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // Run with IDE
            //is Folder an we can list it
            files = new ArrayList<>();
            try {
                for (File f: folder.listFiles()) {
                    if(f.isFile() && f.getAbsolutePath().contains(".jpg"))
                        files.add(new FileInputStream(f));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        for (InputStream fileEntry : files) {
            try {
                BufferedImage image = ImageIO.read(fileEntry);
                ArrayList<Float> img = Image.getFileImgAsFloats(image);
                if ( imgSize != img.size() ) {
                    if (imgSize <= 0) {
                        imgSize = img.size();
                        data = new Data(imgSize);
                        data.setHeight(image.getHeight());
                        data.setWidth(image.getWidth());
                    } else {
                        initialize();
                        LOGGER.log(Level.SEVERE, "Images have different sizes!!!");
                        infoText2.setText("Eror - Images have different sizes!!!");
                        showErrorDialog("Eror - Images have different sizes! Using default data.");
                        throw new IndexOutOfBoundsException("Images have different sizes!!!");
                    }
                }
                data.addVector(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private void showInputImages(Data data) {
        originalImagesHBox.getChildren().clear();
        originalImagesHBox.setSpacing(2);
        int maxImages = ( data.getData().size() > 8 ) ? 8 : data.getData().size();
        for (int i = 0; i < maxImages; i++) {
            BufferedImage img = Image.getFloatsAsGrayImage(data.getData().get(i).getValues(), data.getHeight(), data.getWidth());

            ImageView imageView = new ImageView();
            imageView.setImage(SwingFXUtils.toFXImage(Image.scaleImage(img, 5), null));
            originalImagesHBox.getChildren().add(imageView);
        }
        Label infoShowOnly8Imgs = new Label("Shown "+maxImages+"/"+data.getData().size()+" images.");
        originalImagesHBox.getChildren().add(infoShowOnly8Imgs);
    }

    public void onStopButton() {
        buttonsBox.setDisable(false);
        buttonStop.setDisable(true);
        testTab.setDisable(false);

        training.interrupt();
    }

    public void trainButton() {
        buttonsBox.setDisable(true);
        buttonStop.setDisable(false);
        testTab.setDisable(true);

        setCyclesAndPrecsion();
        runTraining();
    }

    private void runTraining() {
        if( training == null || training.getState() == Thread.State.TERMINATED) {
            training = new Training();
            training.setData(data);
            training.setNet(net);
            training.setOutputImgHBox(outputImgHBox);
            training.setButtonsBox(buttonsBox);
            training.setButtonStop(buttonStop);
            training.setTestTab(testTab);
            training.start();
        } else {
            LOGGER.log(Level.INFO, "The training thread is running already!");
            infoText1.setText("The training thread is running already!");
            showErrorDialog("The training thread is running already!");
        }
    }

    public void doStepButt() {
        net.setCycles(1);
        runTraining();
    }

    public void onSaveNet() {
        FileChooser fileChooser = new FileChooser();

        //Show save file dialog
        File file = fileChooser.showSaveDialog(getStage());

        if (net != null ) {
            LOGGER.log(Level.SEVERE, "Network not initialized!");
            infoText1.setText("Network not initialized!");
            showErrorDialog("Network not initialized!");
        }

        if(file != null ){
            Network.save(net, file.getAbsolutePath());
        }
    }

    public void onLoadNet() {
        FileChooser fileChooser = new FileChooser();

        //Show save file dialog
        File file = fileChooser.showOpenDialog(getStage());

        if(file != null){
            net = Network.loadFromFile(file.getAbsolutePath());
            enableNetFucntionality();
        }
    }

    public void onLoadImage() throws IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showOpenDialog(getStage());

        if(file != null){
            if( net == null) {
                LOGGER.log(Level.SEVERE, "Network not initialized!");
                infoText2.setText("Network not initialized!");
                showErrorDialog("Network not initialized!");
                return;
            }

            ArrayList<Float> imgBeforeNoise = Image.getImgAsFloats(file.getAbsolutePath());
            showAndEvaluateImage(0, imgBeforeNoise);
            loadedImageForEval = imgBeforeNoise;
        }
    }

    private void showAndEvaluateImage(int noise, ArrayList<Float> imgBeforeNoise) {
        ArrayList<Float> imgVals = addNoise(noise, imgBeforeNoise);
        if( imgVals.size() != net.getRealOutput().size()-1) {
            LOGGER.log(Level.SEVERE, "Image has a different size than the trained network!");
            infoText2.setText("Image has a different size than the trained network!");
            showErrorDialog("Image has a different size than the trained network!");
            return;
        }
        BufferedImage img = Image.getFloatsAsGrayImage(imgVals, data.getHeight(), data.getWidth());
        originalImg.setImage(SwingFXUtils.toFXImage(Image.scaleImage(img, 5), null));

        net.addInput(imgVals);
        net.addOutput(imgVals);
        net.feedForward();
        net.calculateError();

        BufferedImage imgReal = Image.getFloatsAsGrayImage(net.getRealOutput(), data.getHeight(), data.getWidth());
        realImg.setImage(SwingFXUtils.toFXImage(Image.scaleImage(imgReal, 5), null));
        imgErrorLabel.setText(String.valueOf(net.getOutputError()));
        SaveResultButt.setDisable(false);
        noiseSlider.setDisable(false);
        noiseLabel.setDisable(false);
    }

    public ArrayList<Float> addNoise(int noise, ArrayList<Float> data) {
        if(noise < 1 || noise > 100) {
            return data;
        }
        ArrayList<Float> newData = new ArrayList<>(data);
        int noisyPixelCount = (this.data.getWidth() * this.data.getHeight() / 100) * noise;
        for (int i = 0; i < noisyPixelCount; i++) {
            int x = randomIntBetween(0, this.data.getWidth()-1);
            int y = randomIntBetween(0, this.data.getHeight()-1);
            float pix = randomFloatBetween(0, 1);
            newData.set(y*this.data.getWidth() + x, pix);
        }
        return newData;
    }

    public void onSaveResult() {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(getStage());

        if(file != null){
            try {
                ImageIO.write(net.getOutputAsImage(data.getWidth(), data.getHeight()), "jpg", file);

                if((int)noiseSlider.getValue() > 0 && loadedImageForEval != null) {
                    File input = new File(file.getAbsolutePath().replace(".jpg", "-input.jpg"));
                    ImageIO.write(Image.getFloatsAsGrayImage(loadedImageForEval, data.getWidth(), data.getHeight()), "jpg", input);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showErrorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }


    public void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Deep Auto Encoder");
        alert.setContentText("Author: Marek Marusic\n" +
                "This is an implementation of an autoencoder.");
        alert.showAndWait();

    }

    private Window getStage() {
        return vbox.getScene().getWindow();
    }

}
