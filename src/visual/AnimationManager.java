package visual;

import co.edu.icesi.tv.game.Game;
import co.edu.icesi.tv.game.GameManagerView;
import co.edu.icesi.tv.game.ImageDictionary;
import android.content.Context;

public class AnimationManager extends GameManagerView {

	private Float[] interactive;

	public AnimationManager(Context context, ImageDictionary imageDictionary,
			long timeThread) {
		super(context, imageDictionary, timeThread);

		interactive = new Float[4];
		interactive[0] = 0f;
		interactive[1] = 0f;
		interactive[2] = 0f;
		interactive[3] = 0f;
		this.setInteractiveObjects(interactive);


		changeToGame(0);
	}

	@Override
	public Game onCreateGame(int gameIndex) {
		return new CompassAnimation(getWidth(), getHeight(), getImageDictionary());
	}
	
	public void setGpsState(boolean isOn) {
		interactive[3] = (isOn ? 1f: 0);
	}

	public void setCompass(float angle) {
		interactive[0] = angle;
	}

	public void setCarPointing(float angle) {
		interactive[1] = angle;
	}

	public void setDistance(float distance) {
		interactive[2] = distance;
	}

	@Override
	public void managerCall() {}

}
