package models.moles;

import models.levels.LocationModel;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;

public class SniffyModel extends MoleModel {

	public SniffyModel(LocationModel location, float speed, float time,
			float appearanceTime, ITiledTextureRegion moleSprite,
			GameScene scene) {
		super(location, speed, time, appearanceTime, moleSprite, scene);
	}

	@Override
	public void onDie() {
		if (!isTouched) {
			gameScene.addToScore(1);
		}
				
		this.destroyMole();
		
		this.gameScene.onMoleDeath();
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if (pSceneTouchEvent.isActionDown()) {
			touched();
			return true;
		}
		return false;
	}
	
	public void touched(){
		gameScene.loseLife();
		isTouched = true;
		onDie();
	}
}



