package oj.utilities;

import com.sun.jna.*;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APITypeMapper;

import oj.data.Model;
import oj.data.WallpaperImage;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static com.sun.jna.platform.win32.WinRas.MAX_PATH;

public class WallpaperSetter {
    private int STRING_OFFSET = 0;
    public WallpaperSetter()
    {
    }

    //setWallpaper(Mat image)
        public boolean setWallpaper(@NotNull WallpaperImage wallpaper)
        {
            final String FILE_EXTENSION = ".jpg";
            //Verify wallpaper has image, if not return false
            if(!wallpaper.hasImage())
                return false;
            //Store Image in App WorkSpace
            String appWorkspace = Model.getPathAppWorkspace() ;
            String fileName = wallpaper.getInfo(WallpaperImage.IMAGE_NAME) + FILE_EXTENSION;
            fileName = RandomStringUtils.randomAlphanumeric(7) + FILE_EXTENSION;
            String filePath = appWorkspace + File.separator + fileName;
            MatOfInt imageQualityParameters = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 100);
            boolean success = Imgcodecs.imwrite(filePath, wallpaper.getImage(), imageQualityParameters);

            if(success)
            {
                setWallpaper(filePath);
                return true;
            }
            else
            {
                return false;
            }
        }

    private void setWallpaper(@NotNull String filePath)
    {
        try {
            //Verify file path exists
            File file = new File(filePath);
            //if file doesn't exist
            if (!file.exists()) {
                throw new FileNotFoundException(this.getClass().getSimpleName()
                        + ": Error - oj.Wallpaper file does not exist");
            }

            filePath = file.getAbsolutePath();
            Pointer filePathPointer = new Memory(MAX_PATH);
            filePathPointer.setString(STRING_OFFSET, filePath);

            NativeWallpaperSetter.INSTANCE.SystemParametersInfoA(
                    NativeWallpaperSetter.SPI_SETDESKWALLPAPER,
                    NativeWallpaperSetter.SPIF_NONE,
                    filePathPointer,
                    NativeWallpaperSetter.SPIF_UPDATEINIFILE
            );
        }

        catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
            System.out.println("Error: File does not exist");
        }
    }

    public String getWallpaper()
    {
        Pointer retrievedPath = new Memory(MAX_PATH);
        NativeWallpaperSetter.INSTANCE.SystemParametersInfoA(
                NativeWallpaperSetter.SPI_GETDESKWALLPAPER,
                NativeWallpaperSetter.SPIF_MAXSTRING,
                retrievedPath,
                NativeWallpaperSetter.SPIF_NOUPDATE
        );
        return retrievedPath.getString(STRING_OFFSET);
    }

    //TODO [TEST]
    private void testGetWallpaper()
    {
        Map<String, Object> map = new HashMap();
        Pointer retrievedPath = new Memory(MAX_PATH);
        map.put(Library.OPTION_TYPE_MAPPER, W32APITypeMapper.DEFAULT);
        NativeWallpaperSetter INSTANCE2 = Native.load("user32",
                NativeWallpaperSetter.class);
        INSTANCE2.SystemParametersInfoA(
                NativeWallpaperSetter.SPI_GETDESKWALLPAPER,
                NativeWallpaperSetter.SPIF_MAXSTRING,
                retrievedPath,
                NativeWallpaperSetter.SPIF_NOUPDATE

        );
//        byte[] bytes = retrievedPath.toString().getBytes(Charset.forName("UTF-16"));
//        String is = new String(bytes, Charset.forName("UTF-16"));
//        SortedMap charSets = Charset.availableCharsets();W
        String string = retrievedPath.getString(0);

        int l = 0;

    }

}

interface NativeWallpaperSetter extends StdCallLibrary {

    //uiAction Options
    final int  SPI_SETDESKWALLPAPER = 0x0014;
    final int SPI_GETDESKWALLPAPER = 0x0073;

    //uiParam Options
    int SPIF_NONE = 0x0000;
    int SPIF_MAXSTRING = MAX_PATH;

    //fWinIni Options
    final int SPIF_NOUPDATE = 0x0000;
    final int SPIF_UPDATEINIFILE = 0x0001;
    final int SPIF_SENDCHANGE = 0x0002;

    //Load appropriate native library (dll)
    NativeWallpaperSetter INSTANCE = Native.load("user32",
            NativeWallpaperSetter.class);

    //Definition of methods to use

    /**
     * Sets or Gets a system parameter, in this case the user's wallpaper.
     * It is a native method of user32.dll library of the Windows platform.
     * @param uiAction The system parameter to get or set. In this case we will
     *                 using either of the following parameters defined in the interface:
     *                 <ul>
     *                 <li>SPI_SETDESKWALLPAPER: to set wallpaper</li>
     *                 <li>SPI_GETDESKWALLPAPER: to get wallpaper</li>
     *                 </ul>
     *                 <p/>
     * @param uiParam The use of this argument depends on the uiAction to be used.
     *                <ul>
     *                <li>if uiAction = SPI_SETDESKWALLPAPER, then set this parameter to 0</li>
     *                <li>if uiAction = SPI_GETDESKWALLPAPER, then set this parameter to size of pvParam
     *                (ie size of String container)</li>
     *                </ul>
     * @param pvParam Use depends on uiAction. Deals with file path of the wallpaper.
     *                <ul>
     *                <li>If uiAction = SPI_SETDESKWALLPAPER, then set this parameter to path
     *                (absolute, preferred) of wallpaper image</li>
     *                <li>If uiAction = SPI_GETDESKWALLPAPER, then the retrieved file path
     *                of current wallpaper is stored in this parameter</li>
     *                </ul>
     * @param fWinIni Parameter to update user profile of change and notify system of change,
     *                <ul>
     *                <li>SPIF_NOUPDATE: Do nothing</li>
     *                <li>SPIF_UPDATEINIFILE: Only update user profile</li>
     *                <li>SPIF_SENDCHANGE: Update user profile and notify system of change</li>
     *                </ul>
     * @return If successful returns true, else returns false
     *
     * For more information refer to:
     * <a>https://docs.microsoft.com/en-us/windows/desktop/api/winuser/nf-winuser-systemparametersinfoa</a>
     */
    boolean SystemParametersInfoA(int uiAction, int uiParam, Pointer pvParam, int fWinIni) ;

}
