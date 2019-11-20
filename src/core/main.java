package core;
import java.util.ArrayList;
import java.util.List;

import toolbox.Utils;

public class main {
	
	private static boolean drawImage = true;
	private static PizzaImage image;
	
	private static int limit = 40;
	
	public static void main(String[] args) {
		// init // load images 
		// input // load data
		// sort-slice
		// draw images
		
		//List<String> inputData = Utils.readAllLines("./input/a_example.in");
		//List<String> inputData = Utils.readAllLines("./input/b_small.in");
		List<String> inputData = Utils.readAllLines("./input/c_medium.in");
		//List<String> inputData = Utils.readAllLines("./input/d_big.in");
		
		image = new PizzaImage();
		
		String[] parameters = inputData.get(0).split(" ");
		
		int rows = Integer.parseInt(parameters[0].trim());
		int columns = Integer.parseInt(parameters[1].trim());
		
		int units = Integer.parseInt(parameters[2].trim());
		int max = Integer.parseInt(parameters[3].trim());
		
		// limit size of processing data set for debug purposes
		if(limit > 0 && limit < rows && limit < columns) {
			rows = limit;
			columns = limit;
		}
		
		print("Tomatoes and Mushrooms per Piecs: " + units + " Max Piece Size: " + max);
		
		List<Mold> molds = crateMolds(units, max);
		
		for (int i = 0; i < molds.size(); i++) {
			Mold mold = molds.get(i);
			//print("Mold " + mold.getRows() + "x" + mold.getColumns());
		}
		print("");
		print("");
		
		int[][] dataMatrix = new int[rows][columns];
		
		// i + 1 because i = 0 its parsing data
		for (int row = 0; row < rows; row++) {
			char[] charArray = inputData.get(row + 1).toCharArray();
			for (int col = 0; col < columns; col++) {
				// tomato == 1, mushroom == 0
				dataMatrix[row][col] = ((charArray[col] == 'T') ? 1 : 0); 
			}
		}
		
		/*
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				System.out.print(dataMatrix[row][col] + " ");
			}
			print();
		} // */
		
		if(drawImage) {
			image.init(dataMatrix);
			image.setMax(rows * columns);
		}
		
		int numOfPices = 0;
		int score = 0;
		
		List<Piece> pieces = slicePizza(dataMatrix, molds, units, max);
		for(Piece piece : pieces) {
			score += piece.getScore();
			numOfPices++;
		}
		
		print("Pieces: " + numOfPices + " Score: " + score);
		
		print("OK!");
		// output 
	}
	
	private static List<Piece> slicePizza(int[][] pizza, List<Mold> molds, int units, int max) {
		
		int pizzaRows = pizza.length;
		int pzzaColumns = pizza[0].length;
		
		List<Piece> pizzaPieces = new ArrayList<Piece>();
		List<SkippedPart> skippedZones = new ArrayList<SkippedPart>();
		
		// In this section we load biggest molds to  start cut off slices
		
		Mold testMaxHorizontalMold = getHorizontal(molds, max);
		Mold testMaxVerticalMold = getVertical(molds, max);
		
		Mold maxHorizontalMold = (testMaxHorizontalMold != null ) ? testMaxHorizontalMold : getPreviousHorizontal(molds, max);
		Mold maxVerticalMold = (testMaxVerticalMold != null ) ? testMaxVerticalMold : getPreviousVertical(molds, max);
		
		int currentMoldXLimit = maxVerticalMold.getColumns();
		int currentMoldYLimit = maxVerticalMold.getRows();
		
		// count maximum possible horizontal and vertical cuts to achieve maximum size of piece
		int xCuts = pzzaColumns / currentMoldXLimit;
		int yCuts = pizzaRows / currentMoldYLimit; // must be x / 1 = x but still...
		
		print("Pizza Col: "		+ pzzaColumns		+ " Pizza Row: "	+ pizzaRows
			+ " MoldLimitX: "	+ currentMoldXLimit	+ " MoldLimitY: "	+ currentMoldYLimit 
			+ " X Cuts: "		+ xCuts				+ " Y Cuts: "		+ yCuts);
		
		//print(x);
		
		int moldCounter = 1;
		
		int multipliedXCuts = xCuts * currentMoldXLimit;
		int multipliedYCuts = yCuts * currentMoldYLimit;
		
		// this loop for horizontal offset (not count columns of slice)
		for(int yCut = 0; yCut < multipliedYCuts; yCut += currentMoldYLimit) {
			
			int displasedYLimit = yCut + currentMoldYLimit;
			
			for(int xCut = 0; xCut < multipliedXCuts; xCut += currentMoldXLimit) {
				
				// process only if this piece in allowed pizzza space, otherwise skipping...
				if(!isExcluded(skippedZones, xCut, yCut)) {
					int displasedXLimit = xCut + currentMoldXLimit;
					
					// count njamnamku
					int mushrooms = 0;
					int tomatoes = 0;
					
					//print("Mold: " + moldCounter++);
					
					// this loop for parse inside current mold
					for(int moldY = yCut; moldY < displasedYLimit; moldY++) {
						for(int moldX = xCut; moldX < displasedXLimit; moldX++) {
							
							int unit = pizza[moldY][moldX];
							
							if(unit == 1) {
								tomatoes++;
							}
							else {
								mushrooms++;
							}
						}
					}
					
					// Ok if Everything of namnashek was more then minimal limit, then adding to all pizza pieces
					if((mushrooms >= units) && (tomatoes >= units)) {
						pizzaPieces.add(new Piece(xCut, yCut, displasedXLimit, displasedYLimit));
						if(drawImage) { image.insertBorder(xCut, yCut, displasedXLimit, displasedYLimit, image.getGreen()); }
					}
					// Something went wrong -> adding to strange zone for future parsing
					else {
						int localMax = (displasedYLimit > displasedXLimit) ? displasedYLimit : displasedXLimit;
						
						skippedZones.add(new SkippedPart(xCut, yCut, localMax, localMax));
						if(drawImage) { image.insertBorder(xCut, yCut, localMax, localMax, image.getRed()); }
					}
					
				}
				
			}
		}
		return pizzaPieces;
	}
	

	
	private static boolean isExcluded(List<SkippedPart> zones, int x, int y) {
		for(SkippedPart zone : zones) {
			if(zone.getTopX() <= x) {
				if(zone.getTopY() <= y) {
					if(zone.getBotX() >= x) {
						if(zone.getBotY() >= y) {
							return true;
						}
					}
				}
			}
					
		}
		return false;
	}
	
	private static List<Mold> crateMolds(int units, int max) {
		List<Mold> molds = new ArrayList<Mold>();
		int filling = units * 2;
		
		while ((filling) <= max) {
			for (int i = 1; i < filling; i++) {
				if((filling % i) == 0) {
					int rows = (filling / i);
					int col = i;
					molds.add(new Mold(rows,col));
				}
			}
			filling++;
		}
		addInverted(molds);
		return molds;
	}
	
	private static Mold getPreviousHorizontal(List<Mold> molds, int size) { // Previous
		while(size > 0) {
			Mold mold = getHorizontal(molds, size);
			if (mold != null) return mold;
			size--;
		}
		return null;
	}
	
	private static Mold getPreviousVertical(List<Mold> molds, int size) { // Previous
		while(size > 0) {
			Mold mold = getVertical(molds, size);
			if (mold != null) return mold;
			size--;
		}
		return null;
	}
	
	private static Mold getHorizontal(List<Mold> molds, int size) {
		for (int i = 0; i < molds.size(); i++) {
			Mold mold = molds.get(i);
			if(mold.getColumns() == size) {
				return mold;
			}
		}
		return null;
	}
	
	private static Mold getVertical(List<Mold> molds, int size) {
		for (int i = 0; i < molds.size(); i++) {
			Mold mold = molds.get(i);
			if(mold.getRows() == size) {
				return mold;
			}
		}
		return null;
	}
	
	private static void addInverted(List<Mold> molds) {
		int startingSize = molds.size();
		for (int i = 0; i < startingSize; i++) {
			Mold mold = molds.get(i);
			
			int rows = mold.getRows();
			int columns = mold.getColumns();
			if(rows != columns) { // work only if columns and rows not the same like 3x3 6x6 9x9 etc...
				boolean found = false;
				
				for (int in = 0; in < startingSize && !found; in++) {
					if(in != i) { // Skipping check itself
						Mold inMold = molds.get(in);
						
						int inRows = inMold.getRows();
						int inColumns = inMold.getColumns();
						if(inRows == columns && inColumns == rows) { // if in array already exist inverted mold -> skip
							found = true;
						}
					}
				}
				if(!found) {
					// adding new inverted mold of current if already not exist in array list
					molds.add(new Mold(columns, rows));
				}
			}
		}
	}
	
	private static String print(String text) {
		System.out.println(text);
		return text;
	}
}
