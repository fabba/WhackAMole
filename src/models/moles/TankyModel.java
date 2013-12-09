package models.moles;

import models.levels.LocationModel;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.whackamole.GameScene;
import com.example.whackamole.ResourcesManager;

public class TankyModel extends MoleModel {

	public TankyModel(LocationModel location, float speed, float time,
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
		HUD gameHUD = gameScene.getGameHUD();
		if(pSceneTouchEvent.isActionDown()){
			if(getCurrentTileIndex() == 0){
				setCurrentTileIndex(1);
				gameScene.addToScore(1);
				return true;
				
			}
			else if(getCurrentTileIndex() == 1){
				setCurrentTileIndex(2);
				gameScene.addToScore(1);
				return true;
				
			}
			else{
				gameHUD.detachChild(this);
		    	gameHUD.unregisterTouchArea(this);
		    	gameScene.addToScore(1);
				return true;
			}
	    	
		}
		return false;
	}
}


