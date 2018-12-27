package oj.utilities;

import com.sun.jna.Library;
import com.sun.jna.Native;

import com.sun.jna.win32.StdCallLibrary;

import com.sun.jna.win32.W32APITypeMapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class WallpaperSetter {

    public WallpaperSetter()
    {
//        setWallpaper("D:\\Users\\Ous\\IdeaProjects\\ShotOnOnePlus Wallpapers\\src" +
//                "\\oj\\res\\image_samples\\LuampaRealImage.jpg");
        getWallpaper();
    }

    public void setWallpaper(@NotNull String filePath)
    {
        try {
            //Verify file path exists
            File file = new File(filePath);
            //if file doesn't exist
            if (!file.exists()) {
                throw new FileNotFoundException(this.getClass().getSimpleName()
                        + ": Error - Wallpaper file does not exist");
            }

            filePath = file.getAbsolutePath();
           boolean success =  NativeWallpaperSetter.INSTANCE.SystemParametersInfoA(
                    NativeWallpaperSetter.SPI_SETDESKWALLPAPER,
                    NativeWallpaperSetter.SPIF_NONE,
                    filePath,
                    NativeWallpaperSetter.SPIF_SENDCHANGE
            );
            System.out.println(success);
        }

        catch (FileNotFoundException ex)
        {
            System.out.println("Error: File does not exist");
        }
    }

    public char[] getWallpaper()
    {
        String retrievedPath = " ";
        boolean success = NativeWallpaperSetter.INSTANCE.SystemParametersInfoA(
                NativeWallpaperSetter.SPI_GETDESKWALLPAPER,
                100,
                retrievedPath,
                NativeWallpaperSetter.SPIF_NOUPDATE

        );

        //String val = String.valueOf(retrievedPath);
        System.out.println(success + ": " + retrievedPath);
        return null;
    }

    public void testGetWallpaper()
    {

    }

}

interface NativeWallpaperSetter extends StdCallLibrary {

    //uiAction Options
    final int  SPI_SETDESKWALLPAPER = 0x0014;
    final int SPI_GETDESKWALLPAPER = 0x0073;

    //uiParam Options
    int SPIF_NONE = 0x0000;
    int SPIF_MAXSTRING = 0x012C;

    //fWinIni Options
    final int SPIF_NOUPDATE = 0x0000;
    final int SPIF_UPDATEINIFILE = 0x0001;
    final int SPIF_SENDCHANGE = 0x0002;

    //Load appropriate native library (dll)
    NativeWallpaperSetter INSTANCE = Native.load("user32",
            NativeWallpaperSetter.class
            );

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
    boolean SystemParametersInfoA(int uiAction, int uiParam, String pvParam, int fWinIni) ;

}
/* @Params of SystemParametersInfoA()
  UINT  uiAction, : A Win32 defined hex code that specifies action to perform. (See @Constants)
  UINT  uiParam, : Depends on uiAction, if
                    + SPI_SETDESKWALLPAPER: uiParam is size of String
                    + SPI_GETDESKWALLPAPER: uiParam is 0
  PVOID pvParam, :  String path of file (Absolute Path)
  UINT  fWinIni : Param to update user profile of change and notify system of change,
                    + SPIF_UPDATEINIFILE: Only update user profile
                    + SPIF_SENDCHANGE: Update user profile and notify system of change


  Note: Java data types are mapped to Native data types
 */

/* @Constants
uiAction :
  - SPI_SETDESKWALLPAPER = 0x0014
  - SPI_GETDESKWALLPAPER = 0x0073

 uiParam:
  - if uiAction = SPI_SETDESKWALLPAPER: None (0)
  - else if uiAction = SPI_GETDESKWALLPAPER: Size of uiParam dataType

 pvParam: String path to wallpaper file

 fWinIni:
  - SPIF_UPDATEINIFILE = 0x0001
  - SPIF_SENDCHANGE = 0x0002


 */
