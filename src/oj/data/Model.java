package oj.data;

import oj.utilities.WallpaperPuller;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

public class Model {

    public static final String HTTPS_PHOTOS_ONEPLUS_COM = "https://photos.oneplus.com";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    public static final String IMAGE_DATE_OF_CAPTURE = "date";
    public static final String IMAGE_NAME = "name";
    public static final String IMAGE_AUTHOR = "author";
    public static final String IMAGE_COUNTRY = "country";

    public static final String[] CJK_COUNTRIES = {"China", "Japan", "Korea", "South Korea", "North Korea"};

    //Fonts
    public static final String FONT_PATH_AILERON = "src\\oj\\res\\fonts\\aileron\\Aileron-Light.otf";
    public static final String FONT_PATH_AFTA = "src\\oj\\res\\fonts\\afta\\AftaSansThin-Regular.otf";
    public static final String FONT_PATH_CJK = "src\\oj\\res\\fonts\\CJK\\I.Ngaan\\I.Ngaan.ttf";

    //Setup Working environment paths for application
    private static String APP_WORKSPACE = "ShotOnOnePlus Wallpapers";
    private static String KEYWORD_OS_APP_DATA = "LOCALAPPDATA";
    private  static String FILE_SEPARATOR = File.separator;
    private static String OS_APP_DATA_PATH = System.getenv(KEYWORD_OS_APP_DATA);

    private static String PATH_APP_WORKSPACE = OS_APP_DATA_PATH + FILE_SEPARATOR
            + APP_WORKSPACE;


    //Resource Paths
    private static String RESOURCE_PATH = "src\\oj\\res";
    private static String APP_ICON_PATH = RESOURCE_PATH + FILE_SEPARATOR + "icons\\ShotOnOnePlus Logo.png";


    @Contract(pure = true)
    public static String getPathAppWorkspace() {
        //Verify path exists
        File appWorkspacePath = new File(PATH_APP_WORKSPACE);
        if(!appWorkspacePath.exists())
        {
            //If not create dir path
            boolean success = appWorkspacePath.mkdir();
            //if mkdir fails, return nothing;
            if(!success)
                return "";
        }
        return PATH_APP_WORKSPACE;
    }

    //TODO: [FAIL, FIX]
    @NotNull
    public static String getRandomString()
    {
        byte[] randomBytes = new byte[10];
        new Random().nextBytes(randomBytes);
        return new String(randomBytes, Charset.forName("UTF-8"));
    }

    public static Image getApplicationIcon()
    {
        File file = new File(APP_ICON_PATH);
        Image image = new Image("file:" + file.getAbsolutePath());
        return image;
    }

    //TODO[TEST Methods]
    public static Mat getLuampaSampleImage()
    {
        String filePath = "D:\\Users\\Ous\\IdeaProjects\\ShotOnOnePlus Wallpapers\\" +
                "src\\oj\\res\\image_samples\\LuampaRealImage.jpg";
        Mat image = Imgcodecs.imread(filePath);
        //HighGui.imshow("Sample Image", image);
        //HighGui.waitKey(10);
        return image;
    }

    public static WallpaperImage getSampleCjkData()
    {
        //24/12 微距 by 汪涛 from China #OneDayOnePhoto#
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put(Model.IMAGE_NAME, "微距" );
        hashMap.put(Model.IMAGE_AUTHOR, "汪涛");
        hashMap.put(Model.IMAGE_COUNTRY, "China");
        hashMap.put(Model.IMAGE_DATE_OF_CAPTURE, "24/12");

        WallpaperImage wallpaperImage = new WallpaperImage();
        Mat image = Imgcodecs.imread("D:\\Users\\Ous\\AppData\\Local\\" +
                "ShotOnOnePlus Wallpapers\\testCjk.jpg");
        wallpaperImage.setImage(image);
        wallpaperImage.setInfo(hashMap);
        return wallpaperImage;
    }

    public static HashMap<String, String> getSampleInfo()
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

