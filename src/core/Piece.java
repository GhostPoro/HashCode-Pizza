package core;
public class Piece {
	
	private int topX;
	private int topY;
	private int botX;
	private int botY;
	
	public Piece(int topX, int topY, int botX, int botY) {
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
	
	public int getScore() {
		return (botX - topX) * (botY - topY);
	}
	
}
