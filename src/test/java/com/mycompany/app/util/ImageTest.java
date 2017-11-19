package com.mycompany.app.util;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 11/19/17.
 */
public class ImageTest {
    @Test
    public void getFloatsAsGrayImage() throws Exception {
        ArrayList<Float> img = Image.getImgAsFloats("batman.jpg");

        Image.getFloatsAsGrayImage(img, 300, 300, 2);
        int i = 0;
    }

}