import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Main extends StateBasedGame {
	public Main(String name) {
		super(name);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer game = new AppGameContainer(new Main("Roguelike"));
		game.setDisplayMode(1280, 1024, true);
		
		game.setVSync(true);
		game.setTargetFrameRate(60);
		game.setMaximumLogicUpdateInterval(10);
		game.setSmoothDeltas(true);
		
		game.start(); 
	}

	public void initStatesList(GameContainer arg0) throws SlickException {
		this.addState(new MainMenuState());
		this.addState(new MainGameState());
		this.addState(new HelpMenuState());
	}
}
