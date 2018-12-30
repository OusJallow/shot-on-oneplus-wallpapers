package oj.utilities;


import oj.data.Model;
import okhttp3.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;


import oj.data.WallpaperImage;
import static javax.imageio.ImageIO.read;

/**
 * Class to pull raw images from ShotOnOnePlus website.
 * Uses okhttp3.
 */
public class WallpaperPuller {

    private OkHttpClient pullerClient;
    private String dailyWallpaperUrl ;
    private String dailyWallpaperInfo;

    public WallpaperPuller() {

        pullerClient = new OkHttpClient();
    }

    /**
     * Pulls the daily wallpaper url and info
     */
    @Nullable
    public WallpaperImage pullWallpaper()
    {
        try{
            if(getDailyWallpaperUrl() == null)
            {
                downloadWallpaperData();
            }

            WallpaperImage wallpaper = new WallpaperImage();
            String wallpaperImageUrl = getDailyWallpaperUrl();
            Mat image = getRawWallpaperImage(wallpaperImageUrl);
            HashMap<String, String> info = getRawWallpaperInfo();

            wallpaper.setImage(image);
            wallpaper.setInfo(info);
            return wallpaper;

        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }

    }

    /* Pull oj.Wallpaper old methods
    TODO: [Review]
    public Mat pullWallpaperF() {

        try {

            if (getDailyWallpaperUrl() == null) {
                downloadWallpaperData();
            }
            String wallpaperUrl= getDailyWallpaperUrl();
            Mat image = getRawWallpaperImage(wallpaperUrl);
            return image;
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public HashMap<String, String> pullWallpaperInfo()
    {
        try {
            if(getDailyWallpaperInfo() == null)
            {
               downloadWallpaperData();
            }
            HashMap<String, String> infoHashmap = getRawWallpaperInfo();
            return infoHashmap;

        }

        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
    }*/

    //TODO: [TEST]
    private void testDownloadWallpaperAndInfo()
    {
//        Mat image = getRawWallpaperImage(getDailyWallpaperUrl());
//        getRawWallpaperInfo();
        //showImage(image);
    }

    /**
     * Gets wallpaper URL and oj.Wallpaper Info from #ShotOnOnePlus website
     */

    private void downloadWallpaperData() throws IOException, BadLocationException {


        //Download website HTML document and parse as string
        Request request;
        String HTTPS_PHOTOS_ONEPLUS_COM = Model.HTTPS_PHOTOS_ONEPLUS_COM;
        String ATTRIBUTE_WALLPAPER_INFO = "data-title";
        String ATTRIBUTE_WALLPAPER_URL = "data-pic";
        String VALUE_CLASS_ATTRIBUTE = "card-item";

        request = new Request.Builder()
                    .url(HTTPS_PHOTOS_ONEPLUS_COM)
                    .build();
        Response response = pullerClient.newCall(request).execute();
        ResponseBody body = response.body();
        String websiteBodyStr = body.string();

        //Create HTML doc object and read website into it
        StringReader stringReader = new StringReader(websiteBodyStr);
        HTMLEditorKit htmlEditorKit = new HTMLEditorKit();
        HTMLDocument htmlDocument = (HTMLDocument) htmlEditorKit.createDefaultDocument();
        htmlEditorKit.read(stringReader, htmlDocument, 0);
        System.out.println(websiteBodyStr);
        //Get Image source url and info(name, photographer, date)
        Element elementWithWallpaperDetails = htmlDocument.getElement(htmlDocument.getDefaultRootElement(),
                    HTML.Attribute.CLASS, VALUE_CLASS_ATTRIBUTE);
        AttributeSet attributeSet = elementWithWallpaperDetails.getAttributes();

        dailyWallpaperUrl = (String) attributeSet.getAttribute(ATTRIBUTE_WALLPAPER_URL);
        dailyWallpaperInfo = (String) attributeSet.getAttribute(ATTRIBUTE_WALLPAPER_INFO);
    }


    /**
     * Downloads oj.Wallpaper Image
     * @param url URL of image
     * @return image - An OpenCV Image matrix or null if retrieval fails
     * @throws Exception Indicates error
     */
    @NotNull
    @Contract("null -> fail")
    private Mat getRawWallpaperImage(String url) throws Exception
    {
        if(url == null)
            throw new Exception("No wallpaper url received");
        Request request = new Request.Builder().url(url).build();
        Response response = pullerClient.newCall(request).execute();
        byte[] image_array = response.body().bytes();
        MediaType mediaType = response.body().contentType();

//        InputStream imageStream = response.body().byteStream();
//        BufferedImage bufferedImage = ImageIO.read(imageStream);

        MatOfByte byteMat = new MatOfByte(image_array);
        Mat image = Imgcodecs.imdecode(byteMat, Imgcodecs.IMREAD_ANYCOLOR);

        //Show Image (For Debug Purposes)
       // HighGui.imshow("Daily Image", image);
       // HighGui.waitKey(5);
        return image;
    }


    /**
     * Extracts image info from image description
     */
    private HashMap<String, String> getRawWallpaperInfo() throws Exception
    {
        //Keywords
        final String WORD_FROM = "from";
        final String WORD_BY = "by";
        final String WORD_ONE_DAY_ONE_PHOTO = "#OneDay";
        final String SPACE_DELIMITER = " ";
        String wallpaperInfo = getDailyWallpaperInfo();
        if(wallpaperInfo == null)
            throw new  Exception(this.getClass().getSimpleName() +  ": No wallpaper info received");

        //Structure of raw Info: [25/11 Pushkar Fair 2018 by Satyam Nain from India #OneDayOnePhoto#]
        String[] infoArray = wallpaperInfo.split(SPACE_DELIMITER);
        int indexOfBy = 0;
        int indexOfFrom = 0 ;
        int indexOfOneDay = 0;

        //Aim is to get:
        //1. Date 2. Name of Photo 3. Author of photo 4. Country
        //Later: 1. Equipment (OnePlus Phone used)

        for(int i = 0; i< infoArray.length; i++ )
        {
            if(infoArray[i].equals(WORD_BY))
                indexOfBy = i;

            if(infoArray[i].equals(WORD_FROM))
                indexOfFrom = i;

            if(infoArray[i].startsWith(WORD_ONE_DAY_ONE_PHOTO))
                indexOfOneDay = i;
        }

        String dateOfCapture = "";
        StringBuilder nameOfImage = new StringBuilder();
        StringBuilder authorOfImage = new StringBuilder() ;
        StringBuilder countryOfImage = new StringBuilder() ;

        for(int i = 0; i <= indexOfOneDay; i++)
        {
            if(i==0)
                dateOfCapture = infoArray[i];

            if(i >= 1 && i< indexOfBy)
                nameOfImage.append(infoArray[i] + SPACE_DELIMITER);

            if(i > indexOfBy && i < indexOfFrom )
                authorOfImage.append(infoArray[i] + SPACE_DELIMITER);

            if(i > indexOfFrom && i < indexOfOneDay)
                countryOfImage.append(infoArray[i] + SPACE_DELIMITER);
        }

        HashMap<String,String> map = new HashMap<String,String>();

        map.put(Model.IMAGE_DATE_OF_CAPTURE, dateOfCapture);
        map.put(Model.IMAGE_NAME, nameOfImage.toString());
        map.put(Model.IMAGE_AUTHOR, authorOfImage.toString());
        map.put(Model.IMAGE_COUNTRY, countryOfImage.toString());
        return map;
    }


    //Properties of Data Members
    @Nullable
    @Contract(pure = true)
    private String getDailyWallpaperUrl() {

        if(dailyWallpaperUrl ==(null))
            return null;
        return dailyWallpaperUrl;
    }


    @Nullable
    @Contract(pure = true)
    private String getDailyWallpaperInfo() {
        if((dailyWallpaperInfo == null))
            return null;

        return dailyWallpaperInfo;
    }


    private void showImage(Mat image, int waitDelay)
    {
        //TODO: [DEBUG]
        //HighGui.namedWindow("Image", HighGui.WINDOW_AUTOSIZE);
        HighGui.imshow("Image", image);
        HighGui.waitKey(waitDelay);
    }

    private void showImage(Mat image)
    {
        //TODO: (REMOVE DEBUG)
//        MatOfInt parameters = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100);
//        boolean write = Imgcodecs.imwrite("LuampaRealImage.jpg", image, parameters);
//        if(write)
//        System.out.println("Image saved");
        showImage(image, 10);
    }



}
