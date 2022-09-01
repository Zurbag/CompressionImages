package com.zu;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;


public class App {
    static JFrame jFrame = getFrame();
    static JPanel jPanel = new JPanel();
    static JSlider jSlider;
    //Переменная нужна для счетчика
    static int fileCounter = 0;

    static String nbsp = "                                             ";
    static JLabel jFileCounterLabel = new JLabel(nbsp+"TOTAL FILES: " + fileCounter+nbsp);
    static JProgressBar jProgressBar;

    static int i = 0;

    public static void main(String[] args) throws IOException {
        //TODO правильно передай туда пути
        Compressor compressor = new Compressor();

        jFrame.add(jPanel);
        //TODO создай кнопку и обработчик для изменения стиля интерфейса

        //Откуда копируем
        JLabel inputLabel = new JLabel("Input directory:   ");
        JTextField inputDirectoryPath = new JTextField(20);
        JButton inputDirectoryButton = new JButton("Browse");
        inputDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(jPanel);
                File file = fileChooser.getSelectedFile();
                inputDirectoryPath.setText(file.getPath());
                compressor.setInPath(inputDirectoryPath.getText());

            }
        });

        //Куда копируем
        JLabel outputLabel = new JLabel("Output directory:");
        JTextField outputDirectoryPath = new JTextField(20);
        JButton outputDirectoryButton = new JButton("Browse");
        outputDirectoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.showOpenDialog(jPanel);
                File file = fileChooser.getSelectedFile();
                outputDirectoryPath.setText(file.getPath());
                compressor.setOutPath(outputDirectoryPath.getText());
            }
        });

        //Слайдер
        jSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 20);
        Border border = BorderFactory.createEtchedBorder();
        Border borderTitle = BorderFactory.createTitledBorder(border, "Compression Quality");
        jSlider.setBorder(borderTitle);
        jSlider.setMinorTickSpacing(25);
        jSlider.setMajorTickSpacing(50);
        jSlider.setPaintTicks(true);
        jSlider.setSnapToTicks(true);

        //Прогресс бар
        jProgressBar = new JProgressBar(0, 2000);
        jProgressBar.setValue(i);
        jProgressBar.setStringPainted(true);
        jProgressBar.setBackground(Color.white);
        jProgressBar.setSize(200, 20);

        //TODO Также можно сделать чекбокс с тем что мы хотим сдеть изображение чернобелым

        //Количество файлов
        jFileCounterLabel.setForeground(Color.RED);

        //Старт сжатия
        JButton compressButton = new JButton("Compress");
        compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compressor.start();
            }
        });

        //Отмена сжатия и выход
        JButton cancelButton = new JButton("  Cancel  ");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int resultChose = JOptionPane.showOptionDialog(jPanel,
                        "Do you want to STOP converting and EXIT?",
                        "Cancel",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
                        null,
                        new String[]{"Yes","No"}, "No");
                if (resultChose == 0) {
                    compressor.stop();
                    System.exit(0);

                }
            }
        });


        jPanel.add(inputLabel);
        jPanel.add(inputDirectoryPath);
        jPanel.add(inputDirectoryButton);

        jPanel.add(outputLabel);
        jPanel.add(outputDirectoryPath);
        jPanel.add(outputDirectoryButton);

        jPanel.add(jSlider);
        jPanel.add(jProgressBar);
        jPanel.add(jFileCounterLabel);

        jPanel.add(compressButton);
        jPanel.add(cancelButton);

        jPanel.revalidate();
        jFrame.revalidate();

    }

    static JFrame getFrame() {
        JFrame jFrame = new JFrame() {
        };
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        jFrame.setBounds(dimension.width / 2 - 230, dimension.height / 2 - 110, 440, 220);
        jFrame.setResizable(false);
        return jFrame;
    }

    @Data
    @NoArgsConstructor
    public static class Compressor extends Thread {
        String inPath = "";
        String outPath = "";
        int compressionQualityFromLabel;

        Compressor(String inPath, String outPath) {
            this.inPath = inPath;
            this.outPath = outPath;
        }

        @Override
        public void run() {

            try {
                compressImage(inPath, outPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void compressImage(String inPath, String outPath) throws IOException {

            //TODO если в целевой папке есть файл файл из исходящей папки не брать

            String path = inPath;
            String newFilePath = outPath;
            Collection<File> files = FileUtils.listFiles(new File(path), new String[]{"jpg"}, true);

            //Устанавливаю изначальное количество файлов на панель
            fileCounter = files.size();
            jProgressBar.setMaximum(files.size());
            jProgressBar.setValue(i);

            jFileCounterLabel.setText(nbsp+"TOTAL FILES: " + fileCounter+nbsp);
            jProgressBar.revalidate();
            jFileCounterLabel.revalidate();

            for (File x : files
            ) {
                String inputPath = x.getPath();
                String outputPath = inputPath.replace(path, newFilePath);
                copyFileUsingApacheCommonsIO(x, new File(outputPath));


                try {
                    File input = x;
                    File output = new File(outputPath);
                    compressAndCopy(input, output);

                    //Меняю счетчик и ревалидирую JPane
                    fileCounter = fileCounter - 1;
                    jFileCounterLabel.setText(nbsp+"TOTAL FILES: " + fileCounter+nbsp);
                    jProgressBar.setValue(++i);



                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
            FileUtils.copyFile(source, dest);
        }

        //Изменение разрешения
        public static BufferedImage resize(BufferedImage image) {

            int width = image.getWidth();
            int height = image.getHeight();
            //1216 x 1728
            Image tmp = null;
            BufferedImage dimg = null;

            //Альбомный
            if (width > height) {
                if (height > 1216) {
                    tmp = image.getScaledInstance(1728, 1216, Image.SCALE_SMOOTH);
                    dimg = new BufferedImage(1728, 1216, BufferedImage.TYPE_INT_RGB);
                } else {
                    tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                }
            }
            //Книжный
            else if (width < height) {
                if (width > 1216) {
                    tmp = image.getScaledInstance(1216, 1728, Image.SCALE_SMOOTH);
                    dimg = new BufferedImage(1216, 1728, BufferedImage.TYPE_INT_RGB);
                } else {
                    tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                }
            }


            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            return dimg;
        }

        //Компрессия и сохранение на диске
        public static void compressAndCopy(File input, File compressedImageFile) throws IOException {
            BufferedImage image = resize(ImageIO.read(input));
            //201308232304284955
            OutputStream fileOutputStream = new FileOutputStream(compressedImageFile);
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
            writer.setOutput(imageOutputStream);
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            float compressionQuality = 0.3f;

            //Логика присвоения значений в степень компрессии
            // a float between 0 and 1 indicating the desired quality level.
            if (jSlider.getValue() > 75) {
                compressionQuality = 1.0f;
            } else if (jSlider.getValue() > 50) {
                compressionQuality = 0.8f;
            } else if (jSlider.getValue() > 25) {
                compressionQuality = 0.7f;
            } else if (jSlider.getValue() > 0) {
                compressionQuality = 0.3f;
            } else if (jSlider.getValue() == 0) {
                compressionQuality = 0.1f;
            }

            param.setCompressionQuality(compressionQuality);
            writer.write(null, new IIOImage(image, null, null), param);

            fileOutputStream.close();
            imageOutputStream.close();
            writer.dispose();
        }
    }

}
