package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class IcyModel extends MoleModel {

	public IcyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_icy, level);
	}
	
	public synchronized void touched(){
		level.freeze(2000);
		level.addToScore(2);
		isTouched = true;
		onDie();
	}
}



