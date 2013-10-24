package me.bloodarowman.bardlike;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Main class for our game, creates a container and runs everything.
 * @since 5/26/13
 * @version 1
 * @author alex
 */
public class Main extends StateBasedGame {
    private static int width, height;
    
    public Main(String name) {
        super(name);
    }

	public static AppGameContainer game;
    public static LuaStateRunnable luaThread;
	public static double scale;

	public static void main() throws SlickException {
        luaThread = new LuaStateRunnable();
        Thread lua = new Thread(luaThread);
        lua.start();

		game = new AppGameContainer(new Main("bardLIKE"));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		scale = (width + height) / 110;

		game.setVSync(true);
		game.setTargetFrameRate(60);
		game.setMaximumLogicUpdateInterval(10);
		game.setSmoothDeltas(true);

        Misc.initLogs();

		game.start();
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(new MainMenuState());
                this.addState(new OptionsMenuState());
		this.addState(new ClassSelectState());
		this.addState(new MainGameState());
		this.addState(new HelpMenuState());
		this.addState(new GameOverState());
	}

    static public class LuaStateRunnable implements Runnable {
        private LuaState L = LuaStateFactory.newLuaState();
        private ArrayList<File> files = new ArrayList<File>();
        private ArrayList<File> permFiles = Misc.findFilesRecurse("./scripts/hooks/update/", "(.*).lua");

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}

                for (File permFile : permFiles) {
                    doExecOfFile(permFile);
                }

                for (File file : files) {
                    doExecOfFile(file);
                }
                files.clear();
            }
        }

        public void doExecOfFile(File f) {
            L.LdoFile(f.toString());
            if (L.status() != 0) {
                System.out.println("Failed execution of: " + f.toString() + " with status: " + L.status());
            }
        }

        public void addFile(File f) {
            files.add(f);
        }

        public void addPermanentFile(File f) {
            permFiles.add(f);
        }

        public LuaState getLuaState() {
            return L;
        }
         
    }
    public static void setResolution(int w, int h) {
        try {
            game.setDisplayMode(w, h, game.isFullscreen());
        } catch (SlickException ex) {
            Misc.showDialog(ex);
        }
    }
}