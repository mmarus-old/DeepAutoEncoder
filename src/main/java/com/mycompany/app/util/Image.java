package com.mycompany.app.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
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

    public static ArrayList<Double> getImgAsDoubles(String fileName) throws IOException {
        ArrayList<Double> vector = new ArrayList<>();

        BufferedImage img = readImage(fileName);
        byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();

        for (byte sample : data) {
            int val = (sample & 0xFF);
            vector.add(Double.valueOf(val / 255.0));
        }

        return vector;
    }

    public static BufferedImage getDoublesAsGrayImage(ArrayList<Double> data, int height, int width) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        byte [] newData = ((DataBufferByte) newImage.getRaster().getDataBuffer()).getData();

        for (int i = 1; i < data.size(); i++)
        {
            newData[i-1] = (byte) ( (int) (data.get(i).doubleValue() * 255) );
        }
        return newImage;
    }

}
