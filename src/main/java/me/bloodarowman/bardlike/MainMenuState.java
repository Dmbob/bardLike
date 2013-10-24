package me.bloodarowman.bardlike;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.FileWriter;
import java.io.InputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * The Main Menu for the game.
 * @author Bobby Henley
 * @since 5/26/13
 * @version 1
 */
public class MainMenuState extends BasicGameState {
	private String menu;
	private Image titleImg;
	private Image backImg;
        private JsonObject savedOptions;
        private boolean optionsLoaded = false;
	public static TrueTypeFont font = new TrueTypeFont(new java.awt.Font("Arial", 1, 20), true);

	@Override
	public void init(GameContainer container, StateBasedGame s) {
		container.setShowFPS(false);
		this.menu = "What Do? [ (P)lay | (H)elp | (O)ptions |(Esc)ape ]";
		try {
			this.titleImg = ImageLoader.loadImage("title.png"); //new Image("./gfx/title.png");
			this.backImg = ImageLoader.loadImage("mainback.png") //new Image("./gfx/mainback.png")
					.getScaledCopy(container.getWidth(), container.getHeight());
                        savedOptions = new GameConfig("/saves/options_save.json").getObject();
		} catch (Exception e) {
			Misc.showDialog(e);
			return;
		}               
		
		GameOverState g = (GameOverState) s.getState(5);
		g.setBackImg(backImg);
		HelpMenuState h = (HelpMenuState) s.getState(3);
		h.setBackImg(backImg);
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g)
			throws SlickException {
		// Background
		g.drawImage(backImg.getScaledCopy(container.getWidth(), container.getHeight()), 0, 0);

		// Icon
		titleImg.setRotation((float) Misc.lerp((double) titleImg.getRotation(), Math.sin(System.currentTimeMillis()/750)*20, 0.01));
		g.drawImage(titleImg, container.getWidth()/2 - titleImg.getWidth()/2, container.getHeight()/2 - titleImg.getHeight()/2);

		// Main menu text
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(menu, container.getWidth()/2 - (font.getWidth(menu))/2, 
				titleImg.getHeight() + (container.getHeight()/2 - 
				titleImg.getHeight()/2));
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) 
			throws SlickException {
                if(!optionsLoaded) {
                    Main.setResolution(savedOptions.get("w").getAsInt(), savedOptions.get("h").getAsInt());
                    container.setFullscreen(savedOptions.get("fullscrn").getAsBoolean());
                    optionsLoaded = true;
                }
		if(s.getCurrentState() == s.getState(1)) {
			if(container.getInput().isKeyPressed(Input.KEY_P)) {
				s.enterState(2); // Jump to main game state.
			} else if (container.getInput().isKeyPressed(Input.KEY_H)) {
				s.enterState(3);
			} else if (container.getInput().isKeyPressed(Input.KEY_Q) ||
					container.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
                                container.exit();
			}else if (container.getInput().isKeyPressed(Input.KEY_O)) {
                                s.enterState(6);
                        }
		}
		container.getInput().clearKeyPressedRecord();
	}

	@Override
	public int getID() {
		return 1;
	}
}
