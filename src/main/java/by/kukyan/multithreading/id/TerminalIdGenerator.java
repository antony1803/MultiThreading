package by.kukyan.multithreading.id;

import java.util.Random;

public class TerminalIdGenerator {
    public static int getId(){
        return new Random(89999).nextInt() + 10000;
    }
}
