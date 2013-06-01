import java.util.ArrayList;

/**
 * A class for randomly generating coherent dungeons.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class DungeonGenerator {
	private static TileDictionary tileDictionary;
	
	public static void generateRoom(ArrayList<ArrayList<Tile>> tiles, int x, int y, int width, int height) {
		//if (height > tiles.size()) { return; }
		for (int x1 = x; x1 < width; x1++) {
			ArrayList<Tile> tileY = new ArrayList<>();
			for (int y1 = y; y1 < height; y1++) {
				if (x1 == 0 || y1 == 0 || x1 == width - 1 || y1 == height - 1) {
					tileY.add(new Tile(tileDictionary, tileDictionary.getRandomWall(), x1, y1));
				} else {
					tileY.add(new Tile(tileDictionary, tileDictionary.getRandomNonwall(), x1, y1));
				}
			}
			tiles.add(tileY);
		}
	}

	public static void generateHallway(int x1, int y1, int x2, int y2) {
	}
	
	public static void placePlayerInFeasibleLocation(ArrayList<ArrayList<Tile>> tiles, Player ply) {
		for (int x = 0; x < tiles.size(); x++) {
			for (int y = 0; y < tiles.get(x).size(); y++) {
				if (tiles.get(x).get(y) == null || tiles.get(x).get(y).getName().equalsIgnoreCase("") || tiles.get(x).get(y).isWall() || (int) (Math.random() * 20) - 1 <= 10) { continue; }
				System.out.println("Placing player at: (" + x + ", " + y + ")");
				ply.setTile(tiles.get(x).get(y));
				return;
			}
		}
	}

	public static ArrayList<ArrayList<Tile>> generateDungeon(int w, int h, TileDictionary tileDictionary) {
		DungeonGenerator.tileDictionary = tileDictionary;

		ArrayList<ArrayList<Tile>> tiles = new ArrayList<>();
		generateRoom(tiles, 0, 0, 10, 10);
		/*String[] temp = new String[tileDictionary.size()];
		temp[0] = "Stone";
		temp[1] = "Wood";
		temp[2] = "Glass";
		temp[3] = "Obsidian";
		for(int x = 0; x < w; x++) {
			ArrayList<Tile> tileY = new ArrayList<>();
			for(int y = 0; y < h; y++) {
				//tileY.add(new Tile(tileDictionary, "Wood", x, y).setSeen(true));
				tileY.add(new Tile(tileDictionary, temp[(int) (Math.random() * temp.length - 1)], x, y).setSeen(true).setInLos(Misc.randomBool()));
			}
			tiles.add(tileY);
		}*/
		return tiles;
	}
}
