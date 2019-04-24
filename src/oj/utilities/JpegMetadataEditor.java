package oj.utilities;

import com.sun.deploy.xml.XMLNode;
import javafx.scene.effect.ImageInput;
import oj.data.Model;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class JpegMetadataEditor {

    final String FORMAT_JPEG_JAVAX_1 = "javax_imageio_jpeg_image_1.0";

    public JpegMetadataEditor()
    {
        testWriteMetadata();
        testReadMetadata();
    }

    //TODO[TESTS]
     void testReadMetadata()
    {
        String filePath = "D:\\Users\\Ous\\AppData\\Local\\ShotOnOnePlus Wallpapers\\arch.jpg";
        readMetadata(filePath);
    }

    void testWriteMetadata()
    {
        String filePath = "D:\\Users\\Ous\\AppData\\Local\\ShotOnOnePlus Wallpapers\\arch.jpg";
        HashMap info = Model.getSampleInfo();
        writeMetadata(filePath, info);
    }

    public  void readMetadata(String filePath)
    {
        try
        {
            File file = new File(filePath);
            ImageInputStream inputStream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
            while (readers.hasNext())
            {
                ImageReader reader  = readers.next();
                String formatName = reader.getFormatName();
                reader.setInput(inputStream);
                IIOMetadata metadata = reader.getImageMetadata(0);
                String[] supportedMetadataFormats = metadata.getMetadataFormatNames();
                String[] extraMetadataFormats = metadata.getExtraMetadataFormatNames();
                String nativeMdF = metadata.getNativeMetadataFormatName();
                IIOMetadataNode node = (IIOMetadataNode)  metadata.getAsTree(supportedMetadataFormats[0]);
                System.out.println(JpegMetadataEditor.toString(node));

                System.out.println("\n\n\n");

                System.out.println(JpegMetadataEditor.toString(metadata.getAsTree(supportedMetadataFormats[1])));
                String string = "do Nothing";


            }
            inputStream.close();
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void writeMetadata(String filePath, HashMap<String, String> imageInfo)
    {
        try
        {

            File file = new File(filePath);
            ImageInputStream inputStream = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> readers = ImageIO.getImageReaders(inputStream);
            if(readers.hasNext())
            {
                ImageReader reader = readers.next();
                reader.setInput(inputStream);
                IIOMetadata metadata = reader.getImageMetadata(0);

                //manipulate metadata, then set
                IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(FORMAT_JPEG_JAVAX_1);
                IIOMetadataNode unknownNode = (IIOMetadataNode) root.getElementsByTagName("unknown").item(0);
                String str = root.getNodeName();
                String f = "";
                System.out.println(JpegMetadataEditor.toString(unknownNode));;


                //set metadata through setFromTree, merge, reset
            }
        }

        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private static String toString(Node node) throws IOException, TransformerException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        return writer.toString();
    }

    public void encodeWallpaperInfoToImageMetadata(Mat img)
    {
        MatOfByte byteImage = new MatOfByte(img);
        byte[] byteArray = byteImage.toArray();
        List byteList = Arrays.asList(byteArray);
        ArrayList<Byte> byteArrayList = new ArrayList<Byte>(byteList);
    }
}
