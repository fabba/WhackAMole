package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class TankyModel extends MoleModel {

	public TankyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_tanky, level);
	}
	
	public synchronized void touched() {
		if (getCurrentTileIndex() == 0) {
			setCurrentTileIndex(1);
			level.addToScore(1);
		}
		else if (getCurrentTileIndex() == 1) {
			setCurrentTileIndex(2);
			level.addToScore(1);
		} else {
			level.addToScore(1);
	    	isTouched = true;
	    	onDie();
		}
	}
}


