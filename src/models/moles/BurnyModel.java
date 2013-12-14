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
	public void onDie() {
		if (!isTouched) {
			gameScene.loseLife();
		}
		
		this.destroyMole();
		
		this.level.onMoleDeath(this);
		
		level.burn();
	}
	
	public void touched() {
		level.addToScore(2);
		isTouched = true;
		this.onDie();
	}
	
}



