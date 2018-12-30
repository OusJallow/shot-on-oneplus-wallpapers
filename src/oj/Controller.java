package oj;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.awt.*;

import oj.Wallpaper;

public class Controller {

    public Wallpaper wallpaper = new Wallpaper();

    @FXML
    public Button pullWallpaperButton;
    public Controller()
    {

    }

    public void pullAndSetWallpaper()
    {
        wallpaper.pullAndSetWallpaper();
    }


}
