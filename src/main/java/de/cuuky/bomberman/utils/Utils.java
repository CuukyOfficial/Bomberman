package de.cuuky.bomberman.utils;

import java.util.Random;

public class Utils {

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}