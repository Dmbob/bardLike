import java.util.ArrayList;

import org.newdawn.slick.Graphics;


public class GameMap {
	private ArrayList<ArrayList<Tile>> tiles; // 2d array list of tiles.
	
	public GameMap(int w, int h) {
	}
	
	public void draw(Graphics g) { // Make it so it only renders near player 3-5 blocks!
		for (int x = 0; x < tiles.size(); x++) {
			ArrayList<Tile> tileX = tiles.get(x);
			for (int y = 0; y < tiles.get(x).size(); y++) {
				Tile tile = tileX.get(y);
				if (tile == null || !tile.getVisible()) { continue; }
				tile.render(g);
			}
		}
	}
	
	public void update() {
		
	}
}
