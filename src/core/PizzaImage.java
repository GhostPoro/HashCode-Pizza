package core;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import toolbox.ImageLoader;
import toolbox.ImageWriter;
import toolbox.Utils;

public class PizzaImage {
	
	private static int[][] fullImage;
	
	private static int[][] convertedTomatoImage;
	private static int[][] convertedMushroomImage;
	
	private static int white;
	private static int black;
	private static int green;
	private static int red;
	
	private int imageCounter = 0;
	private int max = 0;
	
	public PizzaImage() {
		
		BufferedImage tomatoImage   = ImageLoader.loadImage("./res/tomato.png");
		BufferedImage mushroomImage = ImageLoader.loadImage("./res/mushroom.png");
		
		convertedTomatoImage   = ImageLoader.convertTo2DArray(tomatoImage);
		convertedMushroomImage = ImageLoader.convertTo2DArray(mushroomImage);
		
		white = ImageLoader.getFirstPixelRGB(ImageLoader.loadImage("./res/whitePixel.png"));
		black = ImageLoader.getFirstPixelRGB(ImageLoader.loadImage("./res/blackPixel.png"));
		green = ImageLoader.getFirstPixelRGB(ImageLoader.loadImage("./res/greenPixel.png"));
		red   = ImageLoader.getFirstPixelRGB(ImageLoader.loadImage("./res/redPixel.png"));
	}
	
	public void init(int[][] pizza) {
		int imgPartWidth = convertedTomatoImage.length;
		int imgPartHeight = convertedTomatoImage.length;
		
		int pizzaRows = pizza.length;
		int pzzaColumns = pizza[0].length;
		
		// ==== creating basic unprocessed Image ====
		fullImage = new int[pizzaRows * imgPartWidth][pzzaColumns * imgPartHeight];
		
		for(int x = 0; x < pizzaRows; x++) {
			for(int y = 0; y < pzzaColumns; y++) {
				
				int unit = pizza[x][y];
				
				if(unit == 1) {
					for(int mini_x = 0; mini_x < imgPartWidth; mini_x++) {
						for(int mini_y = 0; mini_y < imgPartHeight; mini_y++) {
							fullImage[mini_x + x * imgPartWidth][mini_y + y * imgPartHeight] = convertedTomatoImage[mini_x][mini_y];
						}
					}
				}
				else {
					for(int mini_x = 0; mini_x < imgPartWidth; mini_x++) {
						for(int mini_y = 0; mini_y < imgPartHeight; mini_y++) {
							fullImage[mini_x + x * imgPartWidth][mini_y + y * imgPartHeight] = convertedMushroomImage[mini_x][mini_y];
						}
					}
				}
			}
		}
		
		ImageWriter.save(ImageWriter.createImage(fullImage), new String("Process_" + Utils.numericName(imageCounter++, pizzaRows * pzzaColumns)));
		
		// ==== creating basic unprocessed Image ==== */
		
	}
	
	private void insertBorder(int[][] image, int startX, int startY, int finishX, int finishY, int color) {
		for(int x = startX; x < finishX; x++) {
			for(int y = startY; y < finishY; y++) {
				
				int imgPartWidth = 14;
				int imgPartHeight = 14;
				
				int offsetX = x * imgPartWidth;
				int offsetY = y * imgPartHeight;
				
				for(int mini_x = 0; mini_x < imgPartWidth; mini_x++) {
					for(int mini_y = 0; mini_y < imgPartHeight; mini_y++) {
						if((offsetX + mini_x == startX * imgPartWidth || offsetX + mini_x == finishX * imgPartWidth - 1) || (offsetY + mini_y == startY * imgPartWidth ||  offsetY + mini_y == finishY * imgPartWidth - 1)) {
							fullImage[mini_y + offsetY][mini_x + offsetX] = color;
						}
					}
				}
			}
		}
	}
	
	public void insertBorder(int startX, int startY, int finishX, int finishY, int color) {
		insertBorder(fullImage, startX, startY, finishX, finishY, color);
		ImageWriter.save(ImageWriter.createImage(fullImage), new String("Process_" + Utils.numericName(imageCounter++, max)));
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getWhite() {
		return white;
	}

	public int getBlack() {
		return black;
	}

	public int getGreen() {
		return green;
	}

	public int getRed() {
		return red;
	}
}
