package by.kukyan.multithreading.id;

import java.util.Random;

public class PlateIdGenerator {
    public static String getId(){
        StringBuilder finalId = new StringBuilder("BY");
        Random  rnd = new Random();
        char first = (char) (rnd.nextInt(26) + 'A');
        char second = (char) (rnd.nextInt(26) + 'A');
        finalId.append(rnd.nextInt(8999) + 1000);
        finalId.append(first).append(second);
        return finalId.toString();
    }
}
