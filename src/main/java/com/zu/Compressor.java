package com.zu;
import org.apache.commons.io.FileUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

public class Compressor {
    public void compressImage(String inPath, String outPath) throws IOException {

        String path = inPath;
        String newFilePath = outPath;
//        System.out.println(path);
        Collection<File> files = FileUtils.listFiles(new File(path), new String[]{"jpg"}, true);

        for (File x : files
        ) {
//            System.out.println(x.getName());
            String inputPath = x.getPath();
            String outputPath = inputPath.replaceAll("_archive", "converted_images");
            copyFileUsingApacheCommonsIO(x, new File(outputPath));


            try {
                File input = x;
                File output = new File(outputPath);
                compressAndCopy(input, output);

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
        param.setCompressionQuality(0.3f);
        writer.write(null, new IIOImage(image, null, null), param);

        fileOutputStream.close();
        imageOutputStream.close();
        writer.dispose();
    }
}

