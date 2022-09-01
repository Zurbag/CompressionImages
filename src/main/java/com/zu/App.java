package com.zu;

import java.io.IOException;


public class App {
    public static void main(String[] args) throws IOException {
//        String inPath = "E:\\_archive";
//        String outPath = "E:\\converted_images";
        //new Compressor().compressImage(inPath, outPath);
        MainGUI app = new MainGUI();
        app.setVisible(true);
    }
}
