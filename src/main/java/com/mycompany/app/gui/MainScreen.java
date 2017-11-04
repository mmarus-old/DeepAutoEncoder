package com.mycompany.app.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/30/17.
 */
public class MainScreen extends JPanel {
    BufferedImage image;

    public MainScreen() {
        File imageFile = new File("batman.jpg");

        try {
            image = ImageIO.read(imageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        repaint();

    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
}
