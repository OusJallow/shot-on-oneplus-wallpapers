package oj;

import oj.utilities.*;
import oj.data.*;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.Mat;

public class Wallpaper {

    WallpaperPuller wallpaperPuller;
    WallpaperFitter wallpaperFitter;
    WallpaperSetter wallpaperSetter;
    WallpaperImage wallpaper;

    Wallpaper()
    {
        wallpaperFitter = new WallpaperFitter();
        wallpaperPuller = new WallpaperPuller();
        wallpaperSetter = new WallpaperSetter();
    }

    public void pullAndSetWallpaper()
    {
        wallpaper = wallpaperPuller.pullWallpaper();
        if(!wallpaper.hasImage())
            return;
        Mat image = wallpaperFitter.fitWallpaperToScreen(wallpaper.getImage());
        wallpaper.setImage(image);
        image = wallpaperFitter.drawInfoOnWallpaper(wallpaper);
        wallpaper.setImage(image);
        wallpaperSetter.setWallpaper(wallpaper);


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
