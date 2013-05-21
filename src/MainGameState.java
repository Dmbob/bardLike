import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class MainGameState extends BasicGameState {
	public MainGameState() {
		
	}

	@Override
	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		
	}

	@Override
	public void render(GameContainer container, StateBasedGame s, Graphics g) throws SlickException {
		g.drawString("Main Game goes here", container.getWidth() /2 - 100, container.getHeight()/2);
	}

	@Override
	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		
	}

	@Override
	public int getID() {
		return 2;
	}
}
