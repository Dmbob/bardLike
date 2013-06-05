import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 * A class to handle all the data for tile types.
 * @version 1
 * @since 5/26/13
 * @author alex
 */
public class TileDictionary {
	HashMap<String, Image> imgs;
	HashMap<String, Boolean> walls;

	public TileDictionary() {
		walls = new HashMap<>();
		imgs = new HashMap<>();
		
		try {
			SpriteSheet sprites = new SpriteSheet("./gfx/tiles.png", 32, 32);
			JsonArray tiles = new GameConfig("tiles.json").getArray();
			for (int i = 0; i < tiles.size(); i++) {
				JsonObject tile = tiles.get(i).getAsJsonObject();
				imgs.put(tile.get("name").getAsString(), sprites.getSubImage(tile.get("sx").getAsInt(), tile.get("sy").getAsInt()).getScaledCopy(Misc.TargetSize, Misc.TargetSize));
				
				Boolean isWall = tile.get("wall").getAsBoolean();
				if (isWall) {
					walls.put(tile.get("name").getAsString(), isWall);
				}
			}
			imgs.put("Overlay", new Image("gfx/tile_overlay.png"));
			imgs.put("Empty", new Image("gfx/tile_overlay.png"));
			walls.put("Overlay", true);
			walls.put("Empty", true);
		} catch (Exception e) {
			Misc.showDialog(e);
			return;
		}
	}

	public int size() {
		return imgs.size();
	}

	public boolean getTileIsWall(String name) {
		Boolean isWall = walls.get(name);
		if (isWall != null) { return true; }
		return false;
	}

	public Image getTileImageByName(String name) {
		return imgs.get(name);
	}

	public String getRandomWall() {
		for (Map.Entry<String, Boolean> ele : walls.entrySet()) {
			if (ele.getKey().equalsIgnoreCase("Empty") || ele.getKey().equalsIgnoreCase("Overlay") || (int) (Math.random() * 2) - 1 == 0) { continue; }
				return ele.getKey();
		}
		return "Stone";
	}
	
	public String getRandomNonwall() {
		for (Map.Entry<String, Image> ele : imgs.entrySet()) {
			if (walls.get(ele.getKey()) != null || (int) (Math.random() * 2) - 1 == 0) { continue; }
			return ele.getKey();
		}
		return "Wood";
	}
}
