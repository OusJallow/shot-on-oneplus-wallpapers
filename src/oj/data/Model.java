package oj.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;


import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

public class Model {

    public static final String HTTPS_PHOTOS_ONEPLUS_COM = "https://photos.oneplus.com";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
            "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
    public static final String IMAGE_DATE_OF_CAPTURE = "date";
    public static final String IMAGE_NAME = "name";
    public static final String IMAGE_AUTHOR = "author";
    public static final String IMAGE_COUNTRY = "country";

    private static String KEYWORD_OS_APP_DATA = "LOCALAPPDATA";

    //Setup Working environment paths for application
    private static String APP_WORKSPACE = "ShotOnOnePlus Wallpapers";
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

}

