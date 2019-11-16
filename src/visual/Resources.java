package visual;

import com.mountainreacher.carfinder.R;
import co.edu.icesi.tv.game.ImageDictionary;

public class Resources extends ImageDictionary {

	public Resources(android.content.res.Resources resources) {
		super(2, resources);
		
		imageNames[0] = R.drawable.blue_car_left_small;
		imageNames[1] = R.drawable.radar;
		
	}

}
