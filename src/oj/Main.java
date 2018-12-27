package oj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import oj.utilities.WallpaperFitter;
import oj.utilities.WallpaperPuller;
import oj.utilities.WallpaperSetter;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import java.util.HashMap;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //launch(args);
//        WallpaperPuller wallpaperPuller = new WallpaperPuller();
//        Mat image =wallpaperPuller.pullWallpaper();
//        HashMap<String, String> hashMap =   wallpaperPuller.pullWallpaperInfo();
//        String done = "str";
//        HighGui.namedWindow("Image", HighGui.WINDOW_AUTOSIZE);
//        HighGui.imshow("Image", image);
//        HighGui.waitKey();

        WallpaperSetter wallpaperSetter = new WallpaperSetter();
    }
}

/*
TODO: Basic Features
1. Pull image and info
2. Crop to Screen res
3. Apply Name and Info on Image
4. Set as wallpaper
 */

/*
TODO: GUI
1. Taskbar icon
2. Pull Image Button Trigger
 */
