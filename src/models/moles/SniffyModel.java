package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

/**
 * A mole model which gives the user points if not touched, and makes the user lose
 * lives if touched.
 */
public class SniffyModel extends MoleModel {

	public SniffyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_sniffy, level);
	}

	@Override
	public synchronized void onDie() {
		if (!this.isDead()) {
			this.isDead = true;
			
			if (!isTouched) {
				level.addToScore(1);
			}
					
			this.destroyMole();
			
			this.level.onMoleDeath(this);
		}
	}
	
	public synchronized void touched() {
		level.loseLives(1);
		isTouched = true;
		onDie();
	}
	
	@Override
	public synchronized void unavoidableTouched() {
		onDie();
	}
}



