package co.edu.icesi.tv.game;


import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public abstract class GameManagerView extends View  {

	private boolean alive;
	private boolean running;
	private boolean changeGame;
	private int gameIndex;
	private Object interactiveObjects[];
	private Game playingGame;
	private ImageDictionary imageDictionary;

	public GameManagerView(final Context context, ImageDictionary imageDictionary, final long timeThread) {
		super(context);

		this.alive = true;
		this.running = true;
		this.changeGame = false;
		this.imageDictionary = imageDictionary;

		new Thread(new Runnable() {
			public void run() {
				while(alive) {
					while(running) {
						try {
							Thread.sleep(timeThread);
							postInvalidate();
							managerCall();
						} catch (InterruptedException e) {}
					}
					synchronized (context) {
						if(!running) {
							try {
								context.wait();
							} catch (InterruptedException e) {}
						}
					}
				}
			}
		}).start();
	}

	public abstract void managerCall();

	/**
	 * Use it to send interactive objects to the game cycle, these objects can be used in the move method.
	 * 
	 * @param interactive
	 */
	public void setInteractiveObjects(Object[] interactive) {
		this.interactiveObjects = interactive;
	}

	public ImageDictionary getImageDictionary() {
		return imageDictionary;
	}

	public void pause() {
		this.running = false;
	}

	public void resume() {
		this.running = true;
		synchronized (getContext()) {
			getContext().notifyAll();
		}
	}

	public void destroy() {
		this.alive = false;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if(changeGame) {
			this.changeGame = false;
			this.playingGame = onCreateGame(gameIndex);
		}

		if(playingGame != null) {
			playingGame.gameCycle(canvas, interactiveObjects);
		}

	}

	/**
	 * Invoke this method from any Game to change to another Game
	 * @param gameIndex
	 */
	public void changeToGame(int gameIndex) {
		this.gameIndex = gameIndex;
		this.changeGame = true;
	}

	/**
	 * Override this method to create new Games when changeToGame(int) method has been invoked.
	 * @param gameIndex
	 * @return You must return the new game to set.
	 */
	public abstract Game onCreateGame(int gameIndex);

}
