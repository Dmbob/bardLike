/*The options menu allows the user to change graphics options.
 * 
 * @author: Bobby Henley
 * @date: 10-24-13
 */
package me.bloodarowman.bardlike;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.FileWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
/**
 *
 * @author Bobby
 */
public class OptionsMenuState extends BasicGameState {
    private int screenWidth, screenHeight;
    private JsonArray optionsObjects;
    private JsonObject optionsText;
    private JsonArray resOptions;
    private static int curResWidth, curResHeight;
    private static boolean fullscrn;
    
    public void init(GameContainer gc, StateBasedGame s) throws SlickException {
        try {
            optionsObjects = new GameConfig("loc/options_en.json").getArray();
        } catch (Exception ex) {
           Misc.showDialog(ex);
        }
        optionsText = optionsObjects.get(0).getAsJsonObject();
        resOptions = optionsObjects.get(1).getAsJsonObject().get("Resolutions").getAsJsonArray();
        screenWidth = gc.getWidth();
        screenHeight = gc.getHeight();
    }

    public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
        g.drawString((optionsText.get("title").getAsString()), screenWidth - g.getFont().getWidth(optionsText.get("title").getAsString())/2, screenHeight/4);
        g.drawString(optionsText.get("res").getAsString(), screenWidth - g.getFont().getWidth(optionsText.get("res").getAsString())/2, screenHeight/3);
        g.drawString((optionsText.get("fullscrn").getAsString()), screenWidth - g.getFont().getWidth(optionsText.get("fullscrn").getAsString())/2, screenHeight/2);
        
    }

    public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
        if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
            try {    
                JsonWriter writer = new JsonWriter(new FileWriter(System.getProperty("user.dir") + "/conf/saves/options_save.json"));
                writer.beginObject();
                writer.name("w").value(curResWidth);
                writer.name("h").value(curResHeight);
                writer.name("fullscrn").value(fullscrn);
                writer.endObject();
                writer.close();            
            }catch(Exception ex) {
                Misc.showDialog(ex);
            }
            s.enterState(1);
        }else if(gc.getInput().isKeyPressed(Input.KEY_Y)) {
            gc.setFullscreen(true);
            fullscrn = true;
        }else if(gc.getInput().isKeyPressed(Input.KEY_N)) {
            gc.setFullscreen(false);
            fullscrn = false;
        }else if(gc.getInput().isKeyPressed(Input.KEY_8)){
           Main.setResolution(resOptions.get(0).getAsJsonObject().get("w").getAsInt(), 
                    resOptions.get(0).getAsJsonObject().get("h").getAsInt());
           curResWidth = 800;
           curResHeight = 600;
        }else if(gc.getInput().isKeyPressed(Input.KEY_1)){
           Main.setResolution(resOptions.get(1).getAsJsonObject().get("w").getAsInt(), 
                    resOptions.get(1).getAsJsonObject().get("h").getAsInt());
           curResWidth = 1024;
           curResHeight = 768;
        }else if(gc.getInput().isKeyPressed(Input.KEY_3)){
           Main.setResolution(resOptions.get(2).getAsJsonObject().get("w").getAsInt(), 
                    resOptions.get(2).getAsJsonObject().get("h").getAsInt());
           curResWidth = 1366;
           curResHeight = 768;
        }else if(gc.getInput().isKeyPressed(Input.KEY_9)){
           Main.setResolution(resOptions.get(3).getAsJsonObject().get("w").getAsInt(), 
                    resOptions.get(3).getAsJsonObject().get("h").getAsInt());
           curResWidth = 1920;
           curResHeight = 1080;
        }
        gc.getInput().clearKeyPressedRecord();
        
        screenWidth = gc.getWidth()/2;
        screenHeight = gc.getHeight()/2;
    }
    
    @Override
    public int getID() {
        return 6;
    }
}
