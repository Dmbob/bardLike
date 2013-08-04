package me.bloodarowman.bardlike;

/**
 * A ORAMOWMDEAWOMDO, K?
 * @author Alex
 */
public class Room {
	private int x,y,w,h,connections,looped;
	private Tile[][] tiles;
	private boolean deleted = false;
	
	public Room(Tile[][] tiles, int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tiles = tiles;
	}
	
	public void addHallway() {
		connections++;
	}
	
	public int getConnections() {
		return connections;
	}
	
	public void delete() {
		for (int x = this.x; x < (this.x + w); x++) {
			for (int y = this.y; y < (this.y + h); y++) {
				tiles[x][y] = new Tile("Empty", x, y);
			}
		}
		this.deleted = true;
	}
	
	public Vector getRandomWall() {
		outerloop:
		for (int x = this.x; x < (this.x + w); x++) {
			innerloop:
			for (int y = this.y; y < (this.y + h); y++) {
				if (x-1 <= -1 || y-1 <= -1 || y+1 > tiles[0].length || x+1 > tiles.length) { continue; } // If oob, no good.
				if (x > this.x && x < (this.x + w) && y > this.y && y < (this.y + h)) { continue; } // If not a wall, skip.
				if ((x == this.x && y == this.y) || (x == this.x + w - 1 && y == this.y) || (x == this.x && y == this.y + h - 1) || (x == this.x + w - 1 && y == this.y + h - 1)) { continue; } // If the tile is corner, no dice.
				if ((int) (Math.random() * 100) + 1 <= 50) { continue; } // Random chance (10% true)
				//System.out.println("vector: x: " + x + ", y: " + y);
				return new Vector(x, y);
			}
		}
		looped++;
		if (looped > 20) { return null; }
		return getRandomWall();
	}
	
	public String toString() {
		if (deleted)
			return "(Deleted Room)";
		return "(Room | x: " + x + ", y: " + y + ", w: " + w + ", h: " + h + ", c: " + connections + ")";
	}
}
