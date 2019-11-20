package toolbox;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ImageWriter {
    // just convenience methods for debug

	// this takes and array of doubles between 0 and 1 and generates a grey scale image from them
    public static BufferedImage createGreyImage(double[][] data) {
    	int sizeX = data.length;
    	int sizeY = data[0].length;
    	
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < sizeY; y++)
        {
        	for (int x = 0; x < sizeX; x++)
        	{
        		float inData = ((float) data[x][y]);
        	  
        		inData = ((inData > 1) ? 1 : inData);
        		inData = ((inData < 0) ? 0 : inData);
        	  
        		Color col = new Color(inData,inData,inData);
        		image.setRGB(x, y, col.getRGB());
        	}
        }

        return image;
    }
    
	// this takes and array of floats between 0 and 1 and generates a grey scale image from them
    public static BufferedImage createGreyImage(float[][] data) {
    	int sizeX = data.length;
    	int sizeY = data[0].length;
    	
        BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < sizeY; y++)
        {
        	for (int x = 0; x < sizeX; x++)
        	{
        		float inData = data[x][y];
        	  
        		inData = ((inData > 1) ? 1 : inData);
        		inData = ((inData < 0) ? 0 : inData);
        	  
        		Color col = new Color(inData,inData,inData);
        		image.setRGB(x, y, col.getRGB());
        	}
        }
        return image;
    }
    
	public static BufferedImage createImage(int[][] pixelArray) {
		int sizeX = pixelArray.length;
		int sizeY = pixelArray[0].length;
    	
		BufferedImage image = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < sizeY; y++)
		{
			for (int x = 0; x < sizeX; x++)
			{
				image.setRGB(x, y, pixelArray[x][y]); 
			}
		}
		return image;
	}
    
    public static BufferedImage save(BufferedImage image) {
    	return save(image, "autoName");
    }
    
    public static BufferedImage save(BufferedImage image, String name) {
        try {
            // retrieve image
            File outputfile = new File(name + ".png");
            outputfile.createNewFile();

            ImageIO.write(image, "png", outputfile);
            ImageIO.createImageInputStream(image);
        } catch (IOException e) {
            //o no! Blank catches are bad
            throw new RuntimeException("I didn't handle this very well");
        }
        return image;
    }
    
    public static InputStream toInputStream(BufferedImage image) {
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	try { ImageIO.write(image, "png", os); } catch (IOException e) { e.printStackTrace(); }
    	return new ByteArrayInputStream(os.toByteArray());
    }
    
}
