package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class GoldyModel extends MoleModel {

	public GoldyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_goldy, level);
	}

	public void touched(){
		level.addToScore(5);
		isTouched = true;
		this.onDie();
	}
}



