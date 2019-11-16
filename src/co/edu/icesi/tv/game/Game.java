package co.edu.icesi.tv.game;

import android.graphics.Canvas;


/**
 * This class handles the basic cycle of the game also extends of HComponent to be able of
 * works with the set top boxes and provide content for digital TV.
 * @author i2t
 */
public abstract class Game {

	public int width;
	public int height;
	
	/**
	 * Constructor of a new Game
	 * @param windowWidth
	 * @param windowHeight
	 */
	public Game(int windowWidth, int windowHeight) {
		this.width = windowWidth;
		this.height = windowHeight;
	}
	
	public void gameCycle(Canvas canvas, Object[] interactive) {
		refresh();
		move(interactive);
		collision();
		render(canvas);
	}
	
	/**
	 * Refresh the game canvas.
	 * Use this method to refresh the graphic objects that the player don't uses.
	 */
	abstract protected void refresh ();


	/**
	 * Move all the graphic objetcs of the game. The target of this method is the interaction with the player.
	 * In this method you can receive the interactive objects that have been sent to the GameManagerView.
	 * For more information go to setInteractiveObjects(Object[]) in GameManagerView class.
	 * @param pKeyCode  a representative code of a key of the keyboard
	 */
	abstract protected void move (Object[] interactive);


	/**
	 * Check if there is a collision of the game's objects
	 */
	abstract protected void collision ();


	/**
	 * Paint all the graphic objects of the game
	 */
	abstract protected void render (Canvas canvas);


}