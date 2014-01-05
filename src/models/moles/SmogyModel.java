package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

/**
 * A mole model which triggers a visual smog effect when touched.
 */
public class SmogyModel extends MoleModel {

	public SmogyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_smogy, level);
	}
	
	public synchronized void touched() {
		level.smog(3000l);
		level.addToScore(2);
		isTouched = true;
		onDie();
	}
	
	@Override
	public synchronized void unavoidableTouched() {
		level.addToScore(2);
		isTouched = true;
		onDie();
	}
}



