//package com.zu;
//
//import org.apache.commons.io.FileUtils;
//
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.stream.ImageOutputStream;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Collection;
//import java.util.Iterator;
//
//public class MainGUI extends JFrame {
//    private JTextField textSrcPath = new JTextField("", 5);
//    private JTextField textDestPath = new JTextField("", 5);
//    private JLabel lblSrc = new JLabel("Sours path:");
//
//    private JButton btnChoseSrcPath = new JButton("Browse");
//    private JButton btnStart = new JButton("Compress");
//    private JLabel lblDst = new JLabel("Destination path:");
//    private JButton btnClose = new JButton("Close");
//
//    private  JFileChooser fileChooser = null;
//
//    public MainGUI() {
//        //Наименование
//        super("Image Compressor");
//        //Размеры
//        this.setBounds(400, 300, 440, 180);
//        //Действие призакрытии
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        //Сюда помещаем наши кнопки
//        Container container = this.getContentPane();
//        //Устанавливаем размеры контейнера внутри формы
//        //В радах и колонки и отступы
//        container.setLayout(null);
//        //Помещаем элементы в контейнер
//
//        lblSrc.setBounds(20, 20, 100, 20);
//        container.add(lblSrc);
//
//        textSrcPath.setBounds(150, 20, 120, 20);
//        container.add(textSrcPath);
//
//        //Кнопка Выбора директории исходников
//        btnChoseSrcPath.addActionListener(new ChooseSrcPathListener());
//        btnChoseSrcPath.setBounds(280,20,120,20);
//        container.add(btnChoseSrcPath);
//
////        fileChooser.setBounds(1,1, 120, 20);
////        container.add(fileChooser);
//
//        //Lable and Text
//        lblDst.setBounds(20, 60, 100, 20);
//        container.add(lblDst);
//        textDestPath.setBounds(150, 60, 120, 20);
//        container.add(textDestPath);
//
//        //Кнопка старт
//        btnStart.addActionListener(new StartButtonEventListener());
//        btnStart.setBounds(20,100,120,20);
//        container.add(btnStart);
//
//        //Кнопка Stop And Close
//        btnClose.addActionListener(new StopButtonEventListener());
//        btnClose.setBounds(150,100,120,20);
//        container.add(btnClose);
//
//
//    }
//
//    class StartButtonEventListener implements ActionListener{
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            JOptionPane.showMessageDialog(null, "Start compression image", "",JOptionPane.PLAIN_MESSAGE);
//            try {
//                String inPath = "C:\\_archive";
//                String outPath = "C:\\converted_images";
//                compressImage(inPath,outPath);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//            //Показываем сообщение
//            JOptionPane.showMessageDialog(null, "Ent compression", "",JOptionPane.PLAIN_MESSAGE);
//        }
//    }
//
//    class StopButtonEventListener implements ActionListener{
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            System.exit(0);
//        }
//    }
//
//    class ChooseSrcPathListener implements ActionListener{
//        public void actionPerformed(ActionEvent e) {
//            fileChooser.setDialogTitle("Выбор директории");
//            // Определение режима - только каталог
//            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//            int result = fileChooser.showOpenDialog(fileChooser);
//            // Если директория выбрана, покажем ее в сообщении
//            if (result == JFileChooser.APPROVE_OPTION )
//                JOptionPane.showMessageDialog(fileChooser,
//                        fileChooser.getSelectedFile());
//        }
//    }
//
//    public void compressImage(String inPath, String outPath) throws IOException {
//
//        String path = inPath;
//        String newFilePath = outPath;
////        System.out.println(path);
//        Collection<File> files = FileUtils.listFiles(new File(path), new String[]{"jpg"}, true);
//
//        for (File x : files
//        ) {
////            System.out.println(x.getName());
//            String inputPath = x.getPath();
//            String outputPath = inputPath.replaceAll("_archive", "converted_images");
//            copyFileUsingApacheCommonsIO(x, new File(outputPath));
//
//
//            try {
//                File input = x;
//                File output = new File(outputPath);
//                compressAndCopy(input, output);
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
//        FileUtils.copyFile(source, dest);
//    }
//
//    //Изменение разрешения
//    public static BufferedImage resize(BufferedImage image) {
//
//        int width = image.getWidth();
//        int height = image.getHeight();
//        //1216 x 1728
//        Image tmp = null;
//        BufferedImage dimg = null;
//
//        //Альбомный
//        if (width > height) {
//            if (height > 1216) {
//                tmp = image.getScaledInstance(1728, 1216, Image.SCALE_SMOOTH);
//                dimg = new BufferedImage(1728, 1216, BufferedImage.TYPE_INT_RGB);
//            } else {
//                tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//                dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            }
//        }
//        //Книжный
//        else if (width < height) {
//            if (width > 1216) {
//                tmp = image.getScaledInstance(1216, 1728, Image.SCALE_SMOOTH);
//                dimg = new BufferedImage(1216, 1728, BufferedImage.TYPE_INT_RGB);
//            } else {
//                tmp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
//                dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            }
//        }
//
//
//        Graphics2D g2d = dimg.createGraphics();
//        g2d.drawImage(tmp, 0, 0, null);
//        g2d.dispose();
//
//        return dimg;
//    }
//
//    //Компрессия и сохранение на диске
//    public static void compressAndCopy(File input, File compressedImageFile) throws IOException {
//        BufferedImage image = resize(ImageIO.read(input));
//        //201308232304284955
//        OutputStream fileOutputStream = new FileOutputStream(compressedImageFile);
//        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
//        ImageWriter writer = (ImageWriter) writers.next();
//        ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream);
//        writer.setOutput(imageOutputStream);
//        ImageWriteParam param = writer.getDefaultWriteParam();
//        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//        param.setCompressionQuality(0.3f);
//        writer.write(null, new IIOImage(image, null, null), param);
//
//        fileOutputStream.close();
//        imageOutputStream.close();
//        writer.dispose();
//    }
//}
