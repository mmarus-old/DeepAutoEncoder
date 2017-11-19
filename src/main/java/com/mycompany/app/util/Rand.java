package com.mycompany.app.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Marek Marusic <mmarusic@redhat.com> on 10/21/17.
 */
public class Rand {
    public static float lambda = 1.0f;
    public static float micro = 0.1f;

    public static float randomFloatBetween(float lowerBound, float upperBound) {
        return (float) ThreadLocalRandom.current().nextDouble(lowerBound, upperBound);
    }

    public static float randomIntBetween(int lowerBound, int upperBound) {
        return ThreadLocalRandom.current().nextInt(lowerBound, upperBound+1);
    }
}
