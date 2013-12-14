package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class SmogyModel extends MoleModel {

	public SmogyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_smogy, level);
	}
	
	public synchronized void touched(){
		level.smog();
		level.addToScore(2);
		isTouched = true;
		onDie();
	}
}



