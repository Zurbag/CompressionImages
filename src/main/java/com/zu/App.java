package com.zu;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class App {
    static JFrame jFrame = getFrame();
    static JPanel jPanel = new JPanel();

    public static void main(String[] args) throws IOException {
//        String inPath = "E:\\_archive";
//        String outPath = "E:\\converted_images";
//        new Compressor().compressImage(inPath, outPath);
        jFrame.add(jPanel);
        //TODO создай кнопку и обработчик для изменения стия интерфейса
        JLabel inputLabel = new JLabel("Input directory:   ");
        JTextField inputDirectoryPath = new JTextField(20);
        JButton inputDirectoryButton = new JButton("Browse");

        JLabel outputLabel = new JLabel("Output directory:");
        JTextField outputDirectoryPath = new JTextField(20);
        JButton outputDirectoryButton = new JButton("Browse");

        //TODO Возможно тут нужно сделать ползунок со степенью компрессии

        //TODO Также можно сделать чекбокс с тем что мы хотим сдеть изображение чернобелым

        //TODO тут в отлеьном потоке должна зпуститься компрессия
        JButton compressButton = new JButton("Compress");

        //TODO тут старбатывает jPopupMenu с вопросом точно ли отменить
        JButton cancelButton = new JButton("  Cancel  ");



        jPanel.add(inputLabel);
        jPanel.add(inputDirectoryPath);
        jPanel.add(inputDirectoryButton);

        jPanel.add(outputLabel);
        jPanel.add(outputDirectoryPath);
        jPanel.add(outputDirectoryButton);

        jPanel.add(compressButton);
        jPanel.add(cancelButton);

        jPanel.revalidate();
        jFrame.revalidate();

    }

    static JFrame getFrame(){
        JFrame jFrame = new JFrame(){};
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width/2-250, dimension.height/2-100, 500, 200);
        return jFrame;
    }
}
