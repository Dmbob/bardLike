import java.util.ArrayList;

/**
 * A class for randomly generating coherent dungeons.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class DungeonGenerator {
	private static TileDictionary tileDictionary;
	private static ItemDictionary itemDictionary;
	private static MobDictionary mobDictionary;
	private static GameMap map;
	
	public static void initTileDictionary(TileDictionary tileDictionary) {
		DungeonGenerator.tileDictionary = tileDictionary;
	}
	
	public static void initItemDictionary(ItemDictionary itemDictionary) {
		DungeonGenerator.itemDictionary = itemDictionary;
	}
	
	public static void initMobDictionary(MobDictionary mobDictionary) {
		DungeonGenerator.mobDictionary = mobDictionary;
	}
	
	public static Room generateRoom(Tile[][] tiles, int x, int y, int width, int height) {
		//if (height > tiles.size()) { return; }
		if (x + width > tiles.length || y + height > tiles[0].length) { return null; }
		Tile[][] room = new Tile[width][height];
		for (int x1 = x; x1 < (x + width); x1++) {
			for (int y1 = y; y1 < (y + height); y1++) {
				Tile tile;
				if (x1 == x || y1 == y || x1 == (x + width) - 1 || y1 == (y + height) - 1) {
					tile = new Tile(tileDictionary, tileDictionary.getRandomWall(), x1, y1);
					//DungeonGenerator.placeTile(tiles, new Tile(tileDictionary, tileDictionary.getRandomWall(), x1, y1));
				} else {
					tile = new Tile(tileDictionary, tileDictionary.getRandomNonwall(), x1, y1);
					//DungeonGenerator.placeTile(tiles, new Tile(tileDictionary, tileDictionary.getRandomNonwall(), x1, y1));
				}
				room[x1][y1] = tile;
			}
		}
		return new Room(room);
	}

	public static void generateHallway(Tile[][] tiles, int x1, int y1, int x2, int y2) {
			ArrayList<PathfindingTile> openList = new ArrayList<>();
			ArrayList<PathfindingTile> closedList = new ArrayList<>();
			
			PathfindingTile curLookingTile = new PathfindingTile(null, x1, y1, 0, 0, 0);
			openList.add(curLookingTile);
			
			int added = 0; // May not be needed?
			
			outerloop: // Loop label.
			while (true) {
				innerloop: // Loop label.
				for (Direction dir : Direction.values()) { // Find adj tiles to starting point.
					Vector vec = Misc.getLocFromDir(curLookingTile.x, curLookingTile.y, dir);
					
					if (vec.getX() == x2 && vec.getY() == y2) { closedList.add(new PathfindingTile(curLookingTile, x2, y2, 0, 0, 0)); break outerloop; }
					
					for (int i = 0; i < closedList.size(); i++) {
						if (vec.getX() == closedList.get(i).x && vec.getY() == closedList.get(i).y) {
							continue innerloop;
						}
					}
					
					for (int i = 0; i < openList.size(); i++) {
						if (vec.getX() == openList.get(i).x && vec.getY() == openList.get(i).y) {
							continue innerloop; // Possibly, might want to remove?
						}
					}
					
					try {
						if (vec.getX() < 0 || vec.getX() > tiles.length || vec.getY() < 0 || vec.getY() > tiles[0].length || (tiles[vec.getX()][vec.getY()].isWall() && tiles[vec.getX()][vec.getY()].isReal())) { continue; }
					} catch (IndexOutOfBoundsException ex) { }
					
					//calculate f, g & h here.
					double g = 10.0;
					double h = Double.MAX_VALUE;
					
					double xDistance = Math.abs(vec.getX() - x2); // Diagonol Shortcut Huerisitic
					double yDistance = Math.abs(vec.getY() - y2);
					if (xDistance > yDistance) {
						h = 14.0 * yDistance + 10.0 * (xDistance-yDistance);
					} else {
						h = 14.0 * xDistance + 10.0 * (yDistance-xDistance);
					}
					
					//double h = 10 * (Math.abs(vec.getX() - x2) + Math.abs(vec.getY() - y2)); // Manhattan Huerisitic

					openList.add(new PathfindingTile(curLookingTile, vec.getX(), vec.getY(), g + h, g, h));
					added++;
				}
				if (added == 0) { return; }
				if (openList.isEmpty()) { return; }
				
				openList.remove(curLookingTile); // Add starting tile to closed list.
				closedList.add(curLookingTile);
				
				int lowest = 0;
				for (int i = 0; i < openList.size(); i++) { // Find lowest cost tile. (Will pick last tile if some are the same.)
					if (openList.get(i).f < openList.get(lowest).f) {
						lowest = i;
					}
				}
				
				curLookingTile = openList.get(lowest); // Set new tile to starting point, remove from open, add to closed.
				closedList.add(curLookingTile);
				openList.remove(lowest);
            }
			
			PathfindingTile tile = closedList.get(closedList.size() - 1);
			while (true) {
				DungeonGenerator.placeTile(tiles, new Tile(tileDictionary, "Wood", tile.x, tile.y));
				for (Direction dir : Direction.values()) {
					Vector vec = Misc.getLocFromDir(tile.x, tile.y, dir);
					try {
						Tile wall = tiles[vec.getX()][vec.getY()]; // Location of tobe wall, get tile there!
						if (wall.isReal() || tile.parent == null && (vec.getX() == tile.parent.x && vec.getY() == tile.parent.y)) { continue; }
					} catch (ArrayIndexOutOfBoundsException ex) { continue; }
					DungeonGenerator.placeTile(tiles, new Tile(tileDictionary, "Stone", vec.getX(), vec.getY()));
				}
				if (tile.parent == null) { break; }
				tile = tile.parent;
			}
	}
	
	public static void placePlayerInFeasibleLocation(Tile[][] tiles, Player ply) {
		boolean placed = false;
		for (int x = 0; x < tiles.length; x++) {
			if ((int) (Math.random() * 100) + 1 <= 90) { continue; }
			for (int y = 0; y < tiles[0].length; y++) {
				if (tiles[x][y] == null || tiles[x][y].getName().equalsIgnoreCase("") || tiles[x][y].isWall() || (int) (Math.random() * 100) + 1 <= 80) { continue; }
			//	System.out.println("Placing player at: (" + x + ", " + y + ")");
				placed = true;
				ply.setTile(tiles[x][y]);
				return;
			}
		}
		try {
			if (!placed) { placePlayerInFeasibleLocation(tiles, ply); }
		} catch (StackOverflowError ex) {
			return;
		}
	}
	
	public static void placeItems(Tile[][] tiles, int w, int h) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				//calculate random shit and if equals other random shit place random item
				if((int) (Math.random() * 100) + 1 <= 10) {
					Item item = itemDictionary.getRandomItem();
					if (item == null || !tiles[x][y].isReal() || tiles[x][y].isWall()) { continue; }
					item.setTile(tiles[x][y]);
				}
			}
		}
	}
	
	public static void placeMobs(Tile[][] tiles, int w, int h) {
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				//calculate random shit and if equals other random shit place random item
				if((int) (Math.random() * 100) + 1 <= 10) {
					Mob mob = mobDictionary.getRandomMob();
					if (mob == null || !tiles[x][y].isReal() || tiles[x][y].isWall()) { continue; }
					mob.setTile(tiles[x][y]);
				}
			}
		}
	}
	
	public static void placeTile(Tile[][] tiles, Tile tile) {
			if (tile.getX() < 0 || tile.getY() < 0 || tile.getName().trim().equalsIgnoreCase("") || tile.getX() >= tiles.length || tile.getY() >= tiles[0].length) { return; }
			// System.out.println("DungeonGenerator.placeTile: Placing tile at " + tile.getX() + ", " + tile.getY());
			tiles[tile.getX()][tile.getY()] = tile;
	}

	public static GameMap generateDungeon(int w, int h, TileDictionary tileDictionary, ItemDictionary itemDictionary, MobDictionary mobDictionary) {
		DungeonGenerator.initTileDictionary(tileDictionary);
		DungeonGenerator.initItemDictionary(itemDictionary);
		DungeonGenerator.initMobDictionary(mobDictionary);
		GameMap empty = new GameMap(w, h, tileDictionary, itemDictionary, mobDictionary);
		
		Tile[][] tiles = new Tile[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				tiles[x][y] = new Tile(tileDictionary, "Empty", x, y); // Layout an entire dungeon full of "Empty" tiles.
			}
		}
		
		ArrayList<Room> rooms = new ArrayList<>();
		
		for (int x = 0; x < tiles.length; x++) {
			for (int y = 0; y < tiles.length; y++) {
				if ((int) (Math.random() * 100) + 1 >= 98) {
					int width = (int) (Math.random() * 7) + 3;
					int height = (int) (Math.random() * 5) + 3;
					rooms.add(generateRoom(tiles, Misc.clamp((int) (Math.random() * tiles.length - width) + 1, 0, 10000), Misc.clamp((int) (Math.random() * tiles.length - width) + 1, 0, 10000), width, height));
				}
			}
		}
		
		//Fill map
		//generateRoom(tiles, 0, 0, tiles.length, tiles.length);
		
		//Debug layout
		//generateRoom(tiles, 0, 0, 10, 10);
		//generateRoom(tiles, 11, 1, 5, 5);
		//generateRoom(tiles, 10, 7, 3, 5);
		//generateHallway(tiles, 1, 9, 13, 1);
		
		placeItems(tiles, w, h);
		placeMobs(tiles, w, h);
		
		empty.setTiles(tiles);
		return empty;
	}
}
