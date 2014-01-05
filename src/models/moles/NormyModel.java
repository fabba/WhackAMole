package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

/**
 * Default mole model, no special behavior.
 */
public class NormyModel extends MoleModel {
	
	public NormyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_normy, level);
	}
	
	public synchronized void touched() {
		level.addToScore(1);
		isTouched = true;
    	onDie();
	}
}