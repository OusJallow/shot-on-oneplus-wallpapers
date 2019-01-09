package oj;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.lang.reflect.Method;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import oj.data.Model;
import oj.data.WallpaperImage;
//import org.controlsfx.control.StatusBar;

public class Controller {

    public Wallpaper wallpaper = new Wallpaper(this);


    @FXML private Button pullWallpaperButton;
    @FXML private ProgressIndicator loadingWallpaperIndicator = new ProgressIndicator();
 //   @FXML private StatusBar statusBar;


    Scene uiScene ;
    GridPane gridPaneRoot;

    public Controller()
    {

    }

    public void pullAndSetWallpaper()
    {

       wallpaper.pullAndSetWallpaper();
    }

    public void showProgressIndicator()
    {
        pullWallpaperButton.setVisible(false);
        loadingWallpaperIndicator.setVisible(true);
    }

    public void hideProgressIndicator()
    {
        loadingWallpaperIndicator.setVisible(false);
        pullWallpaperButton.setVisible(true);
    }

    private void initControls()
    {
        uiScene = pullWallpaperButton.getScene();
        gridPaneRoot = (GridPane) uiScene.getRoot();
        loadingWallpaperIndicator.setProgress(-1);
    }




   























    //Tests

    public void cjkTests()
    {
        //CJK test
        WallpaperImage wallpaperImage = Model.getSampleCjkData();
        wallpaper.setWallpaper(wallpaperImage);
    }

//    public void threadTests()
//    {
//        try
//        {
//            Class[] classes = new Class[0];
//            Method pullAndSetWallpaperMethod = this.getClass()
//                    .getMethod("pullAndSetWallpaper", classes);
//            Object[] objects = new Object[1];
//     //       pullAndSetWallpaperMethod.invoke(this, null);
//            WallpaperTaskThread wallpaperTask = new WallpaperTaskThread(pullAndSetWallpaperMethod,
//                     this);
//
//
//            wallpaperTask.execute();
//
//            String string = "pass";
//            string.toString();
//        }
//
//        catch (Exception ex)
//        {
////            System.out.println(this.getClass().getSimpleName()
////                    + ": Error - Method to thread does not exist");
//            ex.printStackTrace();
//        }
//    }


}
