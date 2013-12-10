package models.moles;

import models.levels.LocationModel;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public class GoldyModel extends MoleModel {

	
	public GoldyModel(LocationModel location, float speed, float time,
			float appearanceTime, ITiledTextureRegion moleSprite,
			GameScene scene) {
		super(location, speed, time, appearanceTime, moleSprite, scene);
	}

	public void onDie() {
		HUD gameHUD = gameScene.getGameHUD();
		gameHUD.detachChild(this);
		gameHUD.unregisterTouchArea(this);
	}

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,ITouchArea pTouchArea,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		HUD gameHUD = gameScene.getGameHUD();
		if(pSceneTouchEvent.isActionDown()){
			gameScene.addToScore(5);
			gameHUD.detachChild(this);
			gameHUD.unregisterTouchArea(this);
			return true;
		}
		return false;
	}
}



