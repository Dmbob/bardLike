import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

/**
 * A generic entity class, represents everything that's not a tile.
 * @sicne 5/26/13
 * @version 1
 * @author Alex
 */
public class Entity {
	private static int iid = 1;
	private int id;
	private int x, y;
	private boolean visible = false;
	private Image img;
	private GameMap map;
	private Tile curTile;

	public Entity(Image img) {
		id = iid;
		iid++;
		this.img = img;
	}

	public void draw(Graphics g, int x, int y) {
		if(!visible) { return; }
		g.drawImage(img, x, y);
	}

	public void update(MainGameState mgs) {}
	public void updateAttacks() {}

	public void setTile(Tile tile) {
		if (curTile != null) { getTile().removeEnt(this); } // Remove from old tile, if we had a last tile.
		if (tile == null) { curTile = tile; visible = false; return; } else { visible = true; } // Make sure entity is visible, now that we are on a tile!
		curTile = tile; // Set our current tile.
		tile.addEnt(this); // Add to new tile.
		x = tile.getX() * Misc.TARGET_SIZE; // Translate from tile index, to actual world coords.
		y = tile.getY() * Misc.TARGET_SIZE;
	}

	public Tile getTile() {
		return curTile;
	}
	
	public Image getImage() {
		return img;
	}
	
	public void setMap(GameMap map) {
		this.map = map;
	}

	public GameMap getMap() {
		return map;
	}

	public void setImage(Image img) {
		this.img = img;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPos(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	@Override
	public String toString() {
		return "(Entity | id: " + id + ", x: " + x + ", y: " + y + ")";
	}
}
