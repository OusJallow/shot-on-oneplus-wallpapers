package oj;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.awt.*;

import oj.Wallpaper;
import oj.data.Model;
import oj.data.WallpaperImage;
import oj.utilities.JpegMetadataEditor;

public class Controller {

    public Wallpaper wallpaper = new Wallpaper();

    @FXML
    public Button pullWallpaperButton;
    public Controller()
    {
        tests();
    }

    public void pullAndSetWallpaper()
    {
       wallpaper.pullAndSetWallpaper();
    }


    public void tests()
    {
        //CJK test
        WallpaperImage wallpaperImage = Model.getSampleCjkData();
        wallpaper.setWallpaper(wallpaperImage);
    }


}
