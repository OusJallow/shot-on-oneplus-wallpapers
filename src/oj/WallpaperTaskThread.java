package oj;

import com.victorlaerte.asynctask.AsyncTask;

import java.lang.reflect.Method;

 class WallpaperTaskThread extends AsyncTask<Void, String, Void>
{
    private Method methodToThread;
    private Wallpaper classContext;
    public WallpaperTaskThread(Method methodToThread, Wallpaper classContext)
    {
        this.methodToThread = methodToThread;
        this.classContext = classContext;
    }
    @Override
    public void onPreExecute() {

        //Block UI and Show Load Screen
        this.setDaemon(false);
        classContext.getUiController().showProgressIndicator();

    }

    @Override
    public Void doInBackground(Void... voids)  {
        //Do work
        try{

             methodToThread.invoke(classContext);

        }

        catch (Exception ex)
        {
            String errorMessage = "An unknown error occurred. Please try again.";
            if(ex.getCause().toString().contains("java.net.UnknownHostException"))
            {
                errorMessage = "Downloading Wallpaper Failed. Please check your network connection.";
            }
            Controller.errorViewer(errorMessage);
            classContext.getUiController().clearStatusBar();
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(Void aVoid) {
        //Release UI Thread
        classContext.getUiController().hideProgressIndicator();
    }


    @Override
    public void progressCallback(String ... message) {

    }
}
