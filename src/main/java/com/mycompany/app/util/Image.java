package com.mycompany.app.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 11/2/17.
 */
public class Image {

    public static int[] getImageAsInts(String fileName) throws IOException {
        BufferedImage image = readImage(fileName);
        int w = image.getWidth();
        int h = image.getHeight();
        return image.getRGB(0, 0, w, h, null, 0, w);
    }

    public static BufferedImage readImage(String fileName) throws IOException {
        return ImageIO.read(new File(fileName));
    }

    public static ArrayList<Float> getImgAsFloats(String fileName) throws IOException {
        ArrayList<Float> vector = new ArrayList<>();

        BufferedImage img = readImage(fileName);
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

        for (byte sample : data) {
            int val = (sample & 0xFF);
            vector.add(Float.valueOf( (float) val / 255.0f));
        }

        return vector;
    }

    public static ArrayList<Float> getFileImgAsFloats(BufferedImage img) throws IOException {
        ArrayList<Float> vector = new ArrayList<>();
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

        for (byte sample : data) {
            int val = (sample & 0xFF);
            vector.add(Float.valueOf( (float) val / 255.0f));
        }

        return vector;
    }

    public static BufferedImage getFloatsAsGrayImage(ArrayList<Float> data, int height, int width) {
        return getFloatsAsGrayImage(data, height, width, 1);
    }

    public static BufferedImage getFloatsAsGrayImage(ArrayList<Float> data, int height, int width, int scale) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte [] newData = ((DataBufferByte) newImage.getRaster().getDataBuffer()).getData();
        if((width*height) < data.size()) {
            data.remove(0);
        }
        for (int i = 0; i < data.size(); i++)
        {
            newData[i] = (byte) ( (int) (data.get(i).floatValue() * 255) );
        }
        return newImage;
    }

    public static BufferedImage getFloatsAsGrayImage(float[] data, int height, int width) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte [] newData = ((DataBufferByte) newImage.getRaster().getDataBuffer()).getData();

        for (int i = 1; i < data.length; i++)
        {
            newData[i-1] = (byte) ( (int) (data[i] * 255) );
        }
        return newImage;
    }

    public static BufferedImage scaleImage(BufferedImage img, int scale) {
        BufferedImage newImage = new BufferedImage(img.getWidth()*scale, img.getHeight()*scale, img.getType());
        Graphics g = newImage.createGraphics();
        g.drawImage(img, 0, 0, img.getWidth()*scale, img.getHeight()*scale, null);
        g.dispose();
        return newImage;
    }

}
