package models.moles;

import models.levels.LevelModel;
import models.levels.LocationModel;

import com.example.whackamole.ResourcesManager;

public class SniffyModel extends MoleModel {

	public SniffyModel(LocationModel location, float time,
			float appearanceTime, LevelModel level) {
		super(location, time, appearanceTime,
				ResourcesManager.getInstance().mole_sniffy, level);
	}

	@Override
	public void onDie() {
		if (!isTouched) {
			level.addToScore(1);
		}
				
		this.destroyMole();
		
		this.level.onMoleDeath(this);
	}
	
	public void touched(){
		gameScene.loseLife();
		isTouched = true;
		onDie();
	}
	
	@Override
	public void unavoidableTouched() {
		onDie();
	}
}



