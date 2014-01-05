package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

/**
 * A mole model which freezes the other moles, preventing movement and
 * delaying appearance of future moles for the duration of the freeze.
 * Also triggers a visual effect.
 */
public class IcyModel extends MoleModel {

	public IcyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_icy, level);
	}
	
	public synchronized void touched(){
		level.freeze(2000l);
		level.addToScore(2);
		isTouched = true;
		onDie();
	}
}



