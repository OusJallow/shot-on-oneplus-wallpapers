package oj.utilities;

import java.io.*;
import java.util.Properties;

public class LoadOpenCVLibrary {

    public static void loadOpenCV() {
        try {
            InputStream inputStream = null;
            File fileOut = null;
            String osName = System.getProperty("os.name");
            System.out.println(osName);
            Properties properties = System.getProperties();

            if (osName.startsWith("Windows")) {
                int bitness = Integer.parseInt(System.getProperty("sun.arch.data.model"));
                if (bitness == 32) {
                    inputStream = new FileInputStream(
                            new File("lib\\opencv-native-lib\\x32\\opencv_java400.dll"));
                } else if (bitness == 64) {
                    inputStream = new FileInputStream(
                            new File("lib\\opencv-native-lib\\x64\\opencv_java400.dll"));
                } else {
                    inputStream = new FileInputStream(new File("lib\\opencv-native-lib\\x64\\opencv_java400.dll"));
                }
                fileOut = File.createTempFile("lib", ".dll");
            }
//              else if (osName.equals("Mac OS X")) {
//                inputStream = LoadOpenCVLibrary.class.getResourceAsStream("/opencv/mac/libopencv_java300.dylib");
//                fileOut = File.createTempFile("lib", ".dylib");
//            }

            if (fileOut != null) {
                OutputStream outputStream = new FileOutputStream(fileOut);
                byte[] buffer = new byte[1024];
                int length;

                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

            outputStream.close();
            inputStream.close();
            System.load(fileOut.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}