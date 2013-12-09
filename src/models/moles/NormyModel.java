package models.moles;

import models.levels.LocationModel;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;

public class NormyModel extends MoleModel {

	public NormyModel(LocationModel location, float speed, float time,
			float appearanceTime, ITiledTextureRegion moleSprite,
			GameScene scene) {
		super(location, speed, time, appearanceTime, moleSprite, scene);
	}

	public void onDie() {
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(pSceneTouchEvent.isActionDown()){
			HUD gameHUD = gameScene.getGameHUD();
			gameHUD.detachChild(this);
	    	gameHUD.unregisterTouchArea(this);
	    	
	    	gameScene.addToScore(1);
			return true;
		}
		return false;
	}
}
