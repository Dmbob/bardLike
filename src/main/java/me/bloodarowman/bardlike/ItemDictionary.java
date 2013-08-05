package me.bloodarowman.bardlike;

import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Map.Entry;

/**
 * A class to handle items.
 * 
 * @author Bobby Henley
 * @version 1
 */
public class ItemDictionary {
	private static HashMap<String, Image> itemImages;
	private static HashMap<String, ItemType> itemTypes;
	private static HashMap<String, WeaponType> weaponTypes;
	private static HashMap<Integer, HashMap<String, Image>> scaledImages;
	private static JsonArray items;
	private static JsonObject curItem;
	
	public static void initItemDictionary() {
		itemImages = new HashMap<String, Image>();
		scaledImages = new HashMap<Integer, HashMap<String, Image>>();
		itemTypes = new HashMap<String, ItemType>();
		weaponTypes = new HashMap<String, WeaponType>();
		
		try {
			items = new GameConfig("items.json").getArray();
			SpriteSheet itemSprites = ImageLoader.loadSpritesheet("./gfx/ents/items.png", 32, 32);
			for(int i = 0; i < items.size(); i++) {
				curItem = items.get(i).getAsJsonObject();
				itemTypes.put(curItem.get("name").getAsString(), ItemType.valueOf(curItem.get("type").getAsString()));
				if (curItem.get("sx").getAsInt() < 0 || curItem.get("sx").getAsInt() > itemSprites.getHorizontalCount() || curItem.get("sy").getAsInt() < 0 || curItem.get("sy").getAsInt() > itemSprites.getVerticalCount()) {
					System.out.println(curItem.get("name").getAsString() + " was out of the spritesheet's bounds, using placeholder.");
					itemImages.put(curItem.get("name").getAsString(), Misc.miscImages.get("placeholder"));
				} else {
					itemImages.put(curItem.get("name").getAsString(), itemSprites.getSubImage(curItem.get("sx").getAsInt(), curItem.get("sy").getAsInt()).getScaledCopy(Misc.TARGET_SIZE, Misc.TARGET_SIZE));
				}
				
				String wepType;
				try {
					wepType = curItem.get("type2").getAsString();
				} catch (NullPointerException ex) { continue; }
				if(wepType != null) {
					weaponTypes.put(curItem.get("name").getAsString(), 
							WeaponType.valueOf(wepType));
				}
			}
		} catch (Exception e) {
			Misc.showDialog(e);
		}
		NameGenerator.setNames(new GameConfig("names.json").getObject());
	}
	
	public static ItemType getType(String name) {
		return itemTypes.get(name);
	}
	
	public static WeaponType getWeaponType(String name) {
		return weaponTypes.get(name);
	}
	
	public static void scaleImages(int i) {
		HashMap<String, Image> imgs = new HashMap<String, Image>();
		for(Entry<String, Image> ele : itemImages.entrySet()) {
			imgs.put(ele.getKey(), ele.getValue().getScaledCopy(i, i));
		}
		scaledImages.put(i, imgs);
	}
	
	public static HashMap<String, Image> getScaledImages(int sized) {
		return scaledImages.get(sized);
	}
	
	public static Image getScaledImageByName(int sized, String name) {
		return getScaledImages(sized).get(name);
	}
	
	public static JsonObject getStats(String name) {
		
		for(int i = 0; i < items.size(); i++) {
			JsonObject item =  items.get(i).getAsJsonObject();
			//System.out.println(item);
			if(item.get("name").getAsString().equalsIgnoreCase(name)) {
				return item.get("stats").getAsJsonObject();
			}
		}
		return null;
	}
	
	public static Image getImage(String name) {
		return itemImages.get(name);
	}
	
	public static int size() {
		return items.size();
	}
	
	public static Item getRandomItem() {
		return new Item(items.get((int) (Math.random() * 
				items.size())).getAsJsonObject().get("name").getAsString());
	}
}
