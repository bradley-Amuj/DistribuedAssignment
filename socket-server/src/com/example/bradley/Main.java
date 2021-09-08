package com.example.bradley;

import java.io.IOException;

public class Main {
//establish server connection
    public static void main(String[] args) {
        try {
            //use zero to have a random port generated
            Thread t = new Server(5000);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
