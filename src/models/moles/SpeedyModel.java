package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class SpeedyModel extends MoleModel {

	public SpeedyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_speedy, level);
	}
	
	public void touched(){
		level.addToScore(2);
		isTouched = true;
		onDie();
	}
}



