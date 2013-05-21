import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;


public class Tile {
	private boolean isVisible;
	private boolean playerSaw;
	private boolean isWall;
	private int spriteX, spriteY; // Tile is not aware of it's x and y?
	private ArrayList<Entity> containedEnts;
	private Image spr;
	
	public Tile(SpriteSheet ss, GameConfig data) {
		isWall = data.getValueAsBool("wall");
		spr = ss.getSubImage(data.getValueAsInt("sx"), data.getValueAsInt("sy"));
	}
	
	public void setSeen(boolean bool) {
		playerSaw = bool;
	}
	
	public boolean hasSeen() {
		return playerSaw;
	}
	
	public void setVisible(boolean vis) {
		isVisible = vis;
	}
	
	public boolean getVisible() {
		return isVisible;
	}
	
	public void render(Graphics g) {
		spr.draw();
		for (int i = 0; i < containedEnts.size(); i++) {
			containedEnts.get(i).render(g);
		}
		if (playerSaw) { /* overlay tile with something */ }
	}

	public void update() {
		
	}
}