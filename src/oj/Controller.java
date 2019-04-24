package oj;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;


import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import oj.data.Model;
import oj.data.WallpaperImage;
import oj.events.ChangeStatusEvent;
import oj.events.ChangeStatusHandler;
import oj.utilities.StatusBarCustom;
import org.controlsfx.control.StatusBar;

public class Controller {

    public Wallpaper wallpaper = new Wallpaper(this);


    @FXML private Button pullWallpaperButton;
    @FXML private ProgressIndicator loadingWallpaperIndicator = new ProgressIndicator();
    @FXML private StatusBarCustom statusBar;


    Scene uiScene ;
    BorderPane booderPane;

    public Controller()
    {
        Platform.runLater(() -> {
            initControls();
        });
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

    public void setStatusBarMessage(String message) {
        //  statusBar.setText(message);
        statusBar.addSetChangeStatusListener(statusHandler);
        Platform.runLater(()->{
            this.statusBar.fireEvent(new ChangeStatusEvent(message));
        });

    }


    public  void clearStatusBar()
    {
        Platform.runLater(()->{
            statusBar.fireEvent(new ChangeStatusEvent(" "));
        });
    }

    private void initControls()
    {
        uiScene = pullWallpaperButton.getScene();
        booderPane = (BorderPane) uiScene.getRoot();
        loadingWallpaperIndicator.setProgress(-1);

    }

    private ChangeStatusHandler statusHandler = new ChangeStatusHandler() {

        @Override
        public void handle(ChangeStatusEvent event) {
            String message = event.getMessage();
            statusBar.setText(message);
        }
    };

    public static void errorViewer(String message)
    {
        Platform.runLater(() -> {

            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.show();

        });

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



