package me.bloodarowman.bardlike;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A basic enemy class, attempts to assault the player.
 * @author Alex
 */
public class Mob extends Entity {
	private static final int EXP_MULT = 5;
	private static final Color HP_BAR_BG = new Color(0, 0, 0, 200);
	private static final Color HP_BAR_FG = new Color(255, 0, 0, 200);

	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	private String name;
	private boolean dead, moved = false;
        
	public Mob(MobDictionary mobDictionary, String name) {
		super(mobDictionary.getMobImage(name));
		stats.put("maxhp", mobDictionary.getHealth(name));
		stats.put("curhp", stats.get("maxhp"));
		stats.put("defense", mobDictionary.getStat(name, "def"));
		stats.put("damage", mobDictionary.getStat(name, "atk"));
		stats.put("spawnchance", mobDictionary.getSpawnChance(name));
		this.name = name;
	}
        
	public String getName() {
		return name;
	}
	
	public int getSpawnChance() {
		return stats.get("spawnchance");
	}
	
	public void attack(Entity ent) {
		Player ply = (Player) ent;
		ply.use(this, stats.get("damage"));
	}
        
	public void use(Entity ent, int amt) {
		Player ply = (Player) ent;
		ply.log("You hit " + getName() + " for " + amt + " damage.");
		stats.put("curhp", stats.get("curhp") - amt);
		if (stats.get("curhp") <= 0) {
			die(ent);
		}
	}
        
	public boolean isDead() {
		return dead;
	}
        
	public void die(Entity ent) {
		Player ply = (Player) ent;
		ply.log("You killed " + getName() + ", gaining " + stats.get("damage")*EXP_MULT + "xp!");
		ply.addXP(stats.get("damage")*EXP_MULT);
		dead = true;
		setTile(null);
	}
	
	public void setMoved(boolean bool) {
		moved = bool;
	}

	private double bar_per = 1.0;
	@Override
	public void draw(GameContainer container, StateBasedGame s, Graphics g) {
		super.draw(container, s, g);

		if (stats.get("curhp") == stats.get("maxhp")) { return; } // Don't bother if hp is full.

		Color col = g.getColor();
		g.setColor(HP_BAR_BG);
		g.fillRect(0, 0, this.getImage().getWidth(), 5); // Bar BG
		g.setColor(HP_BAR_FG);
		bar_per = Misc.lerp(bar_per, ((double)stats.get("curhp")/stats.get("maxhp")), 0.2);
		g.fillRect(1, 1, (float) (bar_per * this.getImage().getWidth() - 1), 5 - 1); //Bar FG
		g.setColor(col);
	}

	private ArrayList<Tile> togo = new ArrayList<Tile>();
	@Override
	public void update(GameContainer container, StateBasedGame gc, int delta) {
		if (moved) { return; }
		if ((Math.abs(getTile().getTileX() - MainGameState.current.getPlayer().getTile().getTileX()) + Math.abs(getTile().getTileY() - MainGameState.current.getPlayer().getTile().getTileY())) <= 8) {
            ArrayList<PathfindingTile> tiles = Misc.pathfindTo(getMap(), getTile().getTileX(), getTile().getTileY(), MainGameState.current.getPlayer().getTile().getTileX(), MainGameState.current.getPlayer().getTile().getTileY());
			if (tiles == null || tiles.size() == 1) { return; }
			PathfindingTile tile = tiles.get(tiles.size() - 1); // Get last tile in path.
			while (true) {
				if (tile.getParent().getParent() == null) {
					Tile tile2 = getMap().getTile(tile.getX(), tile.getY());
					ArrayList<Entity> ents = tile2.findType(Player.class);
					ArrayList<Entity> ents2 = tile2.findType(Mob.class);
					if (ents != null || ents2 != null) { break; }
					setTile(tile2);
					moved = true;
					break;
				}
				tile = tile.getParent(); // This causes us to iterate backwards until we reach the root tile!
			}
		} else {
			for (Direction dir : Direction.values()) {
				Tile tile = this.getMap().getTile(Misc.getLocFromDir(this.getTile().getTileX(), this.getTile().getTileY(), dir));
				if (tile == null || tile.isWall() || !tile.isReal()) { continue; }
				togo.add(tile);
			}
			setTile(togo.get((int) (Math.random() * togo.size())));
			togo.clear();
		}
	}

	public void updateAttacks() {
		if (moved) { moved = false; return; }
		for (Direction dir : Direction.values()) {
			Vector vec = Misc.getLocFromDir(getTile().getTileX(), getTile().getTileY(), dir);
			Tile tile = getMap().getTile(vec.getX(), vec.getY());
			if (tile == null) { continue; }
			ArrayList<Entity> players = tile.findType(Player.class);
			if (players != null) { attack(players.get(0)); }
		}
	}
}
