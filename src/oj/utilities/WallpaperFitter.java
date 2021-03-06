package oj.utilities;

import com.sun.org.apache.xpath.internal.operations.Mod;
import oj.data.Model;
import oj.data.WallpaperImage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class WallpaperFitter {

    public WallpaperFitter()
    {
    }

    private void testDrawInfoOnWallpaper()
    {
        //TODO: [TEST]
        Mat image = Model.getLuampaSampleImage();
        HashMap<String, String> info = Model.getSampleInfo();
        drawInfoOnWallpaper(info, image);
        //TODO: [Remove] For Debug purposes
//        HighGui.imshow("Worded Image", image);
//        HighGui.waitKey(1000);
    }

    public  void testCreateFont()
    {
     Font font = createFont(Model.FONT_PATH_CJK);
     font = createFont(Model.FONT_PATH_AILERON);
     font = createFont(Model.FONT_PATH_AFTA);
    }

    @NotNull
    public Mat fitWallpaperToScreen(Mat image)
    {
        //Compare screen size with image size
        int dimensions =  image.dims();
        Size size = image.size();
        Dimension imageSize = new Dimension((int) size.width, (int) size.height);
        Dimension systemScreenSize = getSystemScreenSize();

//        //TODO: [DEBUG]
//        systemScreenSize = new Dimension(1600, 900);

        //TODO: [ERROR CASE] If imageSize < systemScreenSize
        //if systemScreenSize not equal to imageSize crop image
        if(!systemScreenSize.equals(imageSize))
        {
            //Cropping image to systemSize
            int differenceWidth = Math.abs(systemScreenSize.width - imageSize.width) ;
            differenceWidth = differenceWidth/2;
            int differenceHeight = Math.abs(systemScreenSize.height - imageSize.height);
            differenceHeight = differenceHeight/2;
            Rect rect = new Rect(differenceWidth, differenceHeight, systemScreenSize.width,
                    systemScreenSize.height);
            //Imgproc.rectangle(image, rect, new Scalar(218,14,14), 4);
            Mat croppedImage = new Mat(image, rect);

//            // TODO: (REMOVE) For Debug purposes
//            HighGui.namedWindow("Sample Image", HighGui.WINDOW_AUTOSIZE);
//            HighGui.imshow("Sample Image", croppedImage);
//            HighGui.waitKey(10);
            return croppedImage;
        }

        return image;
    }

    public Mat drawInfoOnWallpaper(WallpaperImage wallpaper)
    {
        return  drawInfoOnWallpaper(wallpaper.getAllInfo(), wallpaper.getImage());
    }

    /**
     * Returns a new image with info written (hard  coded) on it
     * @param info The information to show stored in a HashMap
     * @param image The image to be written on
     */
    @NotNull
    public Mat drawInfoOnWallpaper(@NotNull HashMap<String, String> info, @NotNull Mat image)
    {

        String nameOfImage =  info.get(Model.IMAGE_NAME);
        String authorOfImage = info.get(Model.IMAGE_AUTHOR);
        String dateOfCapture = info.get(Model.IMAGE_DATE_OF_CAPTURE);
        String countryOfImage = info.get(Model.IMAGE_COUNTRY);

        //Assume image is already resized
        Size imageSize = image.size();

        //Calculate position of text
        //put in lower right conner
        float percentageOfX = (float)  2/100;
        double positionX = percentageOfX * imageSize.width;

        float percentageOfY = (float) 88/100;
        double positionY = percentageOfY * imageSize.height;

        //As OpenCV provides a very limited set of fonts,
        //we will be using Java graphics class to write on the image.
        try {
            Graphics graphics ;
            BufferedImage bufferedImage;
            // To Change Mat image to bufferedImage so as to make it compatible with Java Graphics
            bufferedImage = matToBufferedImage(image);
            graphics = bufferedImage.getGraphics();

            //Write Info on image
            int DEFAULT_FONT_SIZE = 20;
            String DEFAULT_FONT_NAME = "Sans Serif";
            int DEFAULT_Y_OFFSET = DEFAULT_FONT_SIZE + 10;
            int DEFAULT_FONT_STYLE = Font.PLAIN;

            //TODO [CAUTION] Font file paths
            //using custom fonts
            Font iNgaan = createFont(Model.FONT_PATH_CJK);
            Font aileronCustomFont = createFont(Model.FONT_PATH_AFTA);
            Font aftaCustomFont = createFont( Model.FONT_PATH_AILERON);

            Font nameOfImageFont;
            Font defaultFont;

            if(isCjkCountry(countryOfImage))
            {
                nameOfImageFont = iNgaan.deriveFont(Font.BOLD, 30);
                defaultFont = iNgaan.deriveFont(Font.PLAIN, DEFAULT_FONT_SIZE);
                //DEFAULT_Y_OFFSET = DEFAULT_Y_OFFSET + 3;
            }
            else
            {
                 nameOfImageFont = aileronCustomFont.deriveFont(Font.BOLD, 30);
                 defaultFont = aftaCustomFont.deriveFont(Font.BOLD, DEFAULT_FONT_SIZE);

            }

            graphics.setFont(nameOfImageFont);
            graphics.drawString(nameOfImage, (int) positionX, (int) positionY);

            graphics.setFont(defaultFont);
            graphics.drawString(authorOfImage,
                    (int) positionX, (int)positionY + DEFAULT_Y_OFFSET);
            graphics.drawString(countryOfImage,
                    (int) positionX, (int) positionY + DEFAULT_Y_OFFSET + DEFAULT_Y_OFFSET);

            //Now convert Java bufferedImage to OpenCV Mat image. (Reverse input process)
            Mat drawnImage;
            drawnImage = bufferedImageToMat(bufferedImage);

            //TODO: [DEBUG] Gui img show
            System.out.println("Image Size: " + image.size().width + " x " + image.size().height);
//            HighGui.imshow("New", drawnImage);
//            HighGui.waitKey(1000);

            return drawnImage;
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a bufferedImage version of  an OpenCV Mat image
     * which is compatible with Java Graphics
     * @param image Mat image to convert
     * @returns A bufferedImage object or null
     * if there is an error
     */
    @Nullable
    private BufferedImage matToBufferedImage(@NotNull Mat image)
    {
        BufferedImage bufferedImage;
        try {
            // To Change Mat image to bufferedImage so as to make it compatible with Java Graphics
            MatOfByte byteImage = new MatOfByte();

            //1. Encode Mat image to MatOfByte to be able to get a byte[]
            Imgcodecs.imencode(".jpg", image, byteImage);
            byte[] byteImageArray = byteImage.toArray();
            //Read byte[] image to ByteArrayInputStream
            InputStream inputStream = new ByteArrayInputStream(byteImageArray);
            //Read inputStream into bufferedImage. Apply Graphics methods
            bufferedImage = ImageIO.read(inputStream);
            return bufferedImage;
        }
        catch (Exception ex)
        {
            System.out.println(this.getClass().getSimpleName() +
            " : Error - changing Mat image to BufferedImage ");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a Mat image version of a buffered image compatible
     * with OpenCV
     * @param bufferedImage Image to convert
     * @returns A Mat image or null if there is a conversion error
     */
    @Nullable
    private Mat bufferedImageToMat(@NotNull BufferedImage bufferedImage)
    {
        try
        {
            //Now convert Java bufferedImage to OpenCV Mat image. (Reverse input process)
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            byte[] byteImageOutput =    outputStream.toByteArray();
            Mat matImage = new MatOfByte(byteImageOutput);
            matImage = Imgcodecs.imdecode(matImage, Imgcodecs.IMREAD_ANYCOLOR);
            return matImage;

        }
        catch (Exception ex)
        {
            System.out.println(this.getClass().getSimpleName()
                    + ": Error - converting bufferedImage to Mat image");
            return null;
        }

    }

    private Dimension getSystemScreenSize()
    {
        return  Toolkit.getDefaultToolkit().getScreenSize();
    }

    @Nullable
    private Font createFont(String resourcePath)
    {
        try{
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        Font createdFont;
            createdFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
            return createdFont;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean isCjkCountry(@NotNull String country)
    {
        //Chinese, Japan and Korean need special fonts
        String[] cjkCountries = Model.CJK_COUNTRIES;
        for(String cjkCountry: cjkCountries)
        {
            if(country.contains(cjkCountry))
            {
                return true;
            }
        }
        return false;
    }
}
