package oj.data;

import org.opencv.core.Mat;

import java.util.HashMap;

public  class WallpaperImage
{
    public Mat getImage() {
        return image;
    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public String getInfo(String infoKey) {
        return info.get(infoKey);
    }

    public HashMap<String, String> getAllInfo()
    {
        return info;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public boolean hasImage()
    {
        //if image is null, return false ; Else return false
        return image != null;
    }

    private Mat image;
    private HashMap<String, String> info;
    private String filename;

    public static final String IMAGE_DATE_OF_CAPTURE = "date";
    public static final String IMAGE_NAME = "name";
    public static final String IMAGE_AUTHOR = "author";
    public static final String IMAGE_COUNTRY = "country";
}
