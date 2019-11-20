package core;
public class SkippedPart {
	
	private int topX;
	private int topY;
	private int botX;
	private int botY;
	
	public SkippedPart(int topX, int topY, int botX, int botY) {
		this.topX = topX;
		this.topY = topY;
		this.botX = botX;
		this.botY = botY;
	}

	public int getTopX() {
		return topX;
	}

	public int getTopY() {
		return topY;
	}

	public int getBotX() {
		return botX;
	}

	public int getBotY() {
		return botY;
	}
	
	
}
