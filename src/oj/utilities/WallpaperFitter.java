package oj.utilities;

import oj.data.Model;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WallpaperFitter {

    public WallpaperFitter()
    {
        testDrawInfoOnWallpaper();
    }

    private void testDrawInfoOnWallpaper()
    {
        Mat image = getLuampaSampleImage();
        HashMap info = getSampleInfo();
        drawInfoOnWallpaper(info, image);
//        HighGui.imshow("Worded Image", image);
//        HighGui.waitKey(1000);
    }

    @NotNull
    public Mat fitWallpaperToScreen(Mat image)
    {
        //Compare screen size with image size
        int dimensions =  image.dims();
        Size size = image.size();
        Dimension imageSize = new Dimension((int) size.width, (int) size.height);
        Dimension systemScreenSize = getSystemScreenSize();

        // Debugging
        systemScreenSize = new Dimension(1600, 900);

        //if sizes not equal crop image ?
        if(!systemScreenSize.equals(imageSize))
        {
            //callCropFunction
            //Pass System dimension and picture dimension


            //get new dimensions assuming  dimension is 1600 (width) * 900 (height)
             int differenceWidth = Math.abs(systemScreenSize.width - imageSize.width) ;
            differenceWidth = differenceWidth/2;
            int differenceHeight = Math.abs(systemScreenSize.height - imageSize.height);
            differenceHeight = differenceHeight/2;
            Rect rect = new Rect(differenceWidth, differenceHeight, systemScreenSize.width, systemScreenSize.height);
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


    private void drawInfoOnWallpaper(HashMap<String, String> info, Mat image)
    {
        String nameOfImage =  info.get(Model.IMAGE_NAME);
        String authorOfImage = info.get(Model.IMAGE_COUNTRY);
        String dateOfCapture = info.get(Model.IMAGE_DATE_OF_CAPTURE);
        String countryOfImage = info.get(Model.IMAGE_COUNTRY);

        //Assume image is already resized
        Size imageSize = image.size();

        //Calculate position of text
        //put in lower right conner
        //get 10% dist of image

        float divide10 = (float)  5/100;
        double positionX = divide10 * imageSize.width;

        float divide90 = (float) 95/100;
        double positionY = divide90 * imageSize.height;


        //TODO: {NOTE] Not satisfactory, trying Java Graphics

//        Imgproc.putText(image,
//                nameOfImage,
//                new Point(positionX, positionY), Imgproc.FONT_HERSHEY_PLAIN,
//                3,
//                new Scalar(255,255,255), 3);

        //change image to bufferedImage
        Graphics graphics ;
        BufferedImage bufferedImage;
        MatOfByte byteImage = new MatOfByte();
        Imgcodecs.imencode(".jpg", image, byteImage);
        byte[] arry = byteImage.toArray();

        InputStream inputStream = new ByteArrayInputStream(arry);

        try {
            //TODO: [Review]
            bufferedImage = ImageIO.read(inputStream);
            graphics = bufferedImage.getGraphics();
            graphics.setFont(new Font("Times New Roman", Font.BOLD, 50));
            graphics.drawString("Test Here We Glo", (int) positionX, (int) positionY);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1000);
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            int outputStreamSize = byteArrayOutputStream.size();
            byte[] byteArrayOutput =    byteArrayOutputStream.toByteArray();
            int lenghtOfByteArrayOutput = byteArrayOutput.length;
            Mat drawnImage = new MatOfByte(byteArrayOutput);
            drawnImage = Imgcodecs.imdecode(drawnImage, Imgcodecs.IMREAD_ANYCOLOR);
            System.out.println("Image Size: " + drawnImage.size().width + " x " + drawnImage.size().height);
            HighGui.imshow("New", drawnImage);
            HighGui.waitKey(1000);


        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }



    }

    public Dimension getSystemScreenSize()
    {
        return  Toolkit.getDefaultToolkit().getScreenSize();
    }

    private Mat getLuampaSampleImage()
    {
        String filePath = "D:\\Users\\Ous\\IdeaProjects\\ShotOnOnePlus Wallpapers\\" +
                "src\\oj\\image_samples\\LuampaRealImage.jpg";
        Mat image = Imgcodecs.imread(filePath);
        //HighGui.imshow("Sample Image", image);
        //HighGui.waitKey(10);
        return image;
    }

    private HashMap<String, String> getSampleInfo()
    {

        //17/12 Lahemaa National Park viru raba by Dmitriy Lapkin from Russia #OneDayOnePhoto#
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Model.IMAGE_NAME, "Lahemaa National Park viru raba" );
        hashMap.put(Model.IMAGE_AUTHOR, "Dmitriy Lapkin");
        hashMap.put(Model.IMAGE_COUNTRY, "Russia");
        hashMap.put(Model.IMAGE_DATE_OF_CAPTURE, "17/12");
        return hashMap;
    }
}
