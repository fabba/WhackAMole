package models.moles;

import models.levels.LocationModel;

import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;

public class GoldyModel extends MoleModel {

	public GoldyModel(LocationModel location, float speed, float time,
			float appearanceTime, ITiledTextureRegion moleSprite,
			GameScene scene) {
		super(location, speed, time, appearanceTime, moleSprite, scene);
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if(pSceneTouchEvent.isActionDown()){
			touched();
			return true;
		}
		return false;
	}
	
	public void touched(){
		gameScene.addToScore(5);
		isTouched = true;
		this.onDie();
	}
}



