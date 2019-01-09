package oj;

import com.victorlaerte.asynctask.AsyncTask;
import oj.utilities.*;
import oj.data.*;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;

import java.lang.reflect.Method;

public class Wallpaper  {

    private WallpaperPuller wallpaperPuller;
    private WallpaperFitter wallpaperFitter;
    private WallpaperSetter wallpaperSetter;

    public Controller getUiController() {
        return uiController;
    }

    private Controller uiController;

    Wallpaper(Controller uiController)
    {
        wallpaperFitter = new WallpaperFitter();
        wallpaperPuller = new WallpaperPuller();
        wallpaperSetter = new WallpaperSetter();
        this.uiController = uiController;
    }


    public void pullAndSetWallpaperUnthreaded() //thread
    {
        WallpaperImage wallpaper = wallpaperPuller.pullWallpaper();
        if(!wallpaper.hasImage())
            return;
        Mat image = wallpaperFitter.fitWallpaperToScreen(wallpaper.getImage());
        wallpaper.setImage(image);
        image = wallpaperFitter.drawInfoOnWallpaper(wallpaper);
        wallpaper.setImage(image);
        wallpaperSetter.setWallpaper(wallpaper);
    }

    public void pullAndSetWallpaper()
    {
        try {
                Class[] classes = new Class[0];
            Method pullAndSetWallpaperUnthreaded = this.getClass()
                    .getMethod("pullAndSetWallpaperUnthreaded", classes);
            WallpaperTaskThread taskThread =
                    new WallpaperTaskThread(pullAndSetWallpaperUnthreaded, this);
            taskThread.execute();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setWallpaper(@NotNull WallpaperImage wallpaper)
    {
        Mat image = wallpaperFitter.fitWallpaperToScreen(wallpaper.getImage());
        wallpaper.setImage(image);
        image = wallpaperFitter.drawInfoOnWallpaper(wallpaper);
        wallpaper.setImage(image);
        wallpaperSetter.setWallpaper(wallpaper);
    }

}

