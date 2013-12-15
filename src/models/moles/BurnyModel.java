package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class BurnyModel extends MoleModel {
	
	public BurnyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_burny, level);
	}
	
	@Override
	public synchronized void onDie() {
		if (!this.isDead()) {
			this.isDead = true;
	    		
			if (!isTouched) {
				level.loseLives(1);
			}
			
			this.destroyMole();
			
			this.level.onMoleDeath(this);
			
			level.burn(500l);
		}
	}
	
	public synchronized void touched() {
		level.addToScore(2);
		isTouched = true;
		this.onDie();
	}
	
}



