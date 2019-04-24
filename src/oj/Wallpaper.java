package oj;

import com.victorlaerte.asynctask.AsyncTask;
import oj.utilities.*;
import oj.data.*;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;

import java.lang.reflect.Method;
import java.net.UnknownHostException;

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


    public void pullAndSetWallpaperUnthreaded() throws Exception //thread
    {
        uiController.setStatusBarMessage("Downloading Wallpaper...");
        WallpaperImage wallpaper = wallpaperPuller.pullWallpaper();
        if(!wallpaper.hasImage())
            throw new RuntimeException("Image not downloaded. No image found.");
        uiController.setStatusBarMessage("Fitting Wallpaper...");
        Mat image = wallpaperFitter.fitWallpaperToScreen(wallpaper.getImage());
        wallpaper.setImage(image);
        uiController.setStatusBarMessage("Adding Info to Wallpaper...");
        image = wallpaperFitter.drawInfoOnWallpaper(wallpaper);
        wallpaper.setImage(image);
        uiController.setStatusBarMessage("Setting Wallpaper...");
        wallpaperSetter.setWallpaper(wallpaper);
        uiController.clearStatusBar();
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
            Controller.errorViewer("An unknown error occurred. Please try again");
            uiController.clearStatusBar();
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

